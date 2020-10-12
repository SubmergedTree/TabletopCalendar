package de.submergedtree.tabletopcalendar.gamesession

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GameSessionService {
    fun getSession(gameSessionId: String): Mono<DetailedGameSession>
    fun getSessions(untilFilterTimestamp: String): Flux<DetailedGameSession>
    fun createSession(createGameSession: CreateGameSession): Mono<String>
    fun deleteExpiredSession(daysUntilExpire: Int): Flux<String>
    fun deleteSession(gameSessionId: String): Mono<Void>
}
