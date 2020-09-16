package de.submergedtree.tabletopcalendar.gamesession

import reactor.core.publisher.Mono

interface GameSessionService {
    fun getSession(id: String): Mono<GameSession>
}