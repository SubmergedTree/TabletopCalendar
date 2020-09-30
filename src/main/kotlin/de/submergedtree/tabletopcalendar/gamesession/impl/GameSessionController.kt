package de.submergedtree.tabletopcalendar.gamesession.impl

import de.submergedtree.tabletopcalendar.gamesession.GameSession
import de.submergedtree.tabletopcalendar.gamesession.GameSessionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Component
class GameSessionController(private val gameSessionService: GameSessionService) {

    fun getGameSessionHandler(req: ServerRequest) = ServerResponse.ok()
            .body(gameSessionService.getSession("123"), GameSession::class.java)

    fun getAllGameSessionsHandler(req: ServerRequest) = ServerResponse.ok()
            .body(gameSessionService.getSession("123"), GameSession::class.java)
}