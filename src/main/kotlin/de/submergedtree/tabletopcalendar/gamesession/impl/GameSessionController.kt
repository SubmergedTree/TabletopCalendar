package de.submergedtree.tabletopcalendar.gamesession.impl

import de.submergedtree.tabletopcalendar.gamesession.GameSession
import de.submergedtree.tabletopcalendar.gamesession.GameSessionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class GameSessionController(private val gameSessionService: GameSessionService) {

    @Bean
    fun gameSessionRouter() = router {
        GET("api/session").invoke { req -> sessionHandler(req) }
    }

    fun sessionHandler(req: ServerRequest) = ServerResponse.ok()
            .body(gameSessionService.getSession("123"), GameSession::class.java)

}