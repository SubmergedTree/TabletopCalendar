package de.submergedtree.tabletopcalendar.gamesession.impl

import de.submergedtree.tabletopcalendar.gamesession.GameSession
import de.submergedtree.tabletopcalendar.gamesession.GameSessionService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class InMemoryGameSessionService(): GameSessionService {
    override fun getSession(id: String): Mono<GameSession> {
        return Mono.just(GameSession(id, "player1"))
    }
}