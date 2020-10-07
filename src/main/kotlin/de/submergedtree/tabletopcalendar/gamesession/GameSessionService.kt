package de.submergedtree.tabletopcalendar.gamesession

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

interface GameSessionService {
    fun getSession(gameSessionId: String): Mono<GameSession>
    fun getSessions(untilFilterTimestamp: String): Flux<GameSession>
    fun createSession(gameSession: GameSession): Mono<String>
    fun deleteExpiredSession(daysUntilExpire: Int)
}
