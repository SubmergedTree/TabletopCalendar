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
                .filterWhen{validateUsersIds(it.playerIds)}
                .filterWhen{validateHostUserKey(it.presentedGames)}
                .flatMapMany { Flux.fromIterable(createGameSession.presentedGames) }
                .filterWhen { gameService.validateGameKey(it.gameKey)}
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

    // can we use Spring Verify on data classes ?
    override fun updateSession(updateGameSession: UpdateGameSession): Mono<String> =
        Mono.just(updateGameSession)
                .filterWhen { g -> gameSessionRepository.existsById(g.gameSessionId)}  // does not work
                .filterWhen{validateUsersIds(it.playerIds)}
                .filterWhen{validateHostUserKey(it.presentedGames)}
                .flatMapMany { Flux.fromIterable(it.presentedGames) }
                .filterWhen { gameService.validateGameKey(it.gameKey)}
                .map { GameMongo(it.host, it.gameKey) }
                .collectList()
                .map {
                    GameSessionMongo(updateGameSession.gameSessionId,
                            updateGameSession.gameSessionName,
                            updateGameSession.timestamp,
                            updateGameSession.playerIds,
                            it)
                }.flatMap { gameSessionRepository.save(it) }
                .map { it.gameSessionId }

    private fun validateUsersIds(userIds: List<String>): Mono<Boolean> =
        Flux.fromIterable(userIds)
                .flatMap(userService::validateUserKey)
                .reduce(true, { acc, next -> acc && next})

    private fun validateHostUserKey(presentedGames: List<CreatePresentedGame>): Mono<Boolean> =
        Flux.fromIterable(presentedGames)
                .flatMap { userService.validateUserKey(it.host) }
                .reduce(true, { acc, next -> acc && next})

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


    private fun userIdsToUsers(userIds: List<String>): Flux<User> =
            Flux.fromIterable(userIds)
                    .flatMap { userService.getUser(it) } // TODO should return an anonymous User if no user found for key. -> Ditch user validation in create game Session

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