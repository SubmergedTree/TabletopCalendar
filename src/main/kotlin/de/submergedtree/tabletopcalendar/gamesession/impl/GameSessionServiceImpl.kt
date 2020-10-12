package de.submergedtree.tabletopcalendar.gamesession.impl

import de.submergedtree.tabletopcalendar.game.GameService
import de.submergedtree.tabletopcalendar.gamesession.*
import de.submergedtree.tabletopcalendar.user.UserService
import de.submergedtree.tabletopcalendar.user.impl.User
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.*

@Service
class GameSessionServiceImpl(
        private val gameSessionRepository: GameSessionRepository,
        private val userService: UserService,
        private val gameService: GameService,
        private val identifierService: IdentifierService
): GameSessionService, Logging {

    override fun getSession(gameSessionId: String): Mono<DetailedGameSession> =
        gameSessionRepository.findById(gameSessionId)
                .flatMap(this::toGameSession)

    override fun getSessions(untilFilterTimestamp: String): Flux<DetailedGameSession> =
            gameSessionRepository.getGameSessionByTimestampAfter(untilFilterTimestamp)
                    .flatMap(this::toGameSession)

    override fun createSession(createGameSession: CreateGameSession): Mono<String> =
            Mono.just(createGameSession)
                .flatMap{validateUsersIds(it)} //TODO user validation does not work!
                .flatMap{validateHostUserKey(it)}
                .flatMapMany { Flux.fromIterable(createGameSession.presentedGames) }
                .flatMap { g -> validateGameKey(g)}
                .map { GameMongo(it.host, it.gameKey) }
                .collectList()
                .map {
                    val gameSessionId = identifierService.generate()
                    GameSessionMongo(gameSessionId,
                        createGameSession.gameSessionName,
                        createGameSession.timestamp,
                        createGameSession.playerIds,
                        it)
                }.flatMap { gameSessionRepository.save(it) }
                .map { it.gameSessionId }

    override fun deleteExpiredSession(daysUntilExpire: Int): Flux<String> {
        val deleteAfter = Instant.now().minusSeconds(daysUntilExpire * 86400L)
        return gameSessionRepository.deleteGameSessionByTimestampBefore(deleteAfter.toString())
                .map { it.gameSessionId }
    }

    override fun deleteSession(gameSessionId: String): Mono<Void> =
             gameSessionRepository.deleteById(gameSessionId)

    private fun validateUsersIds(createGameSessions: CreateGameSession): Mono<CreateGameSession> =
        Flux.fromIterable(createGameSessions.playerIds)
                .map(userService::validateUserKey)
             //   .doOnNext{println(it)}
             //   .doOnError { println(it.message) }
                .collectList()
                .map { createGameSessions }

    private fun validateHostUserKey(createGameSessions: CreateGameSession): Mono<CreateGameSession> =
        Flux.fromIterable(createGameSessions.presentedGames)
                .map { userService.validateUserKey(it.host) }
                .collectList()
                .map { createGameSessions }

    private fun toGameSession(gameSessionMongo: GameSessionMongo): Mono<DetailedGameSession> =
         userIdsToUsers(gameSessionMongo.userIds)
                .collectList()
                .zipWith(persistedGamesToPresentedGames(gameSessionMongo.hosts).collectList())
                .map {
                    DetailedGameSession(gameSessionMongo.gameSessionId,
                    gameSessionMongo.gameSessionName,
                    gameSessionMongo.timestamp,
                    it.t1,
                    it.t2)
                }

    private fun validateGameKey(g: CreatePresentedGame) =
        gameService.validateGameKey(g.gameKey).map { g }

    private fun userIdsToUsers(userIds: List<String>): Flux<User> =
            Flux.fromIterable(userIds)
                    .flatMap { userService.getUser(it) }

    private fun persistedGamesToPresentedGames(gameMongo: List<GameMongo>): Flux<PresentedGame> =
            Flux.fromIterable(gameMongo)
                    .flatMap { convertToPresentedGame(it) }

    private fun convertToPresentedGame(gameHost: GameMongo): Mono<PresentedGame> {
        val host = userService.getUser(gameHost.userId)
        val games = gameService.getGame(gameHost.gameSearchKey)
        return host.zipWith(games)
                .map { PresentedGame(it.t1, it.t2) }
    }
}