package de.submergedtree.tabletopcalendar.gamesession.impl

import de.submergedtree.tabletopcalendar.gamesession.GameHost
import de.submergedtree.tabletopcalendar.gamesession.GameSession
import de.submergedtree.tabletopcalendar.gamesession.GameSessionService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class GameSessionServiceImpl(
        private val gameSessionRepository: GameSessionRepository
): GameSessionService {

    override fun getSession(gameSessionId: String): Mono<GameSession> =
        gameSessionRepository.findById(gameSessionId)
                .map(this::convertToGameSession)

    override fun getSessions(untilFilterTimestamp: String): Flux<GameSession> =
            gameSessionRepository.getGameSessionDaosByTimestampAfter(untilFilterTimestamp)
                    .map(this::convertToGameSession)

    override fun createSession(gameSession: GameSession): Mono<String> {
        TODO("Not yet implemented")
    }

    override fun deleteExpiredSession(daysUntilExpire: Int) {
        TODO("Not yet implemented")
    }

    private fun convertToGameSession(gameSessionDao: GameSessionDao): GameSession =
            GameSession(gameSessionId = gameSessionDao.gameSessionId,
            gameSessionName = gameSessionDao.gameSessionName,
            playerIds = gameSessionDao.playerIds,
            timestamp = gameSessionDao.timestamp,
            hosts = gameSessionDao.hosts.map(this::convertToHost))

    private fun convertToHost(gameHostDao: GameHostDao): GameHost =
            GameHost(player = gameHostDao.player, gameIds = gameHostDao.gameIds)

}