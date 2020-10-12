package de.submergedtree.tabletopcalendar.gamesession.web

import de.submergedtree.tabletopcalendar.security.CalendarIdentifierWebFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class GameSessionRouter(
        private val filter: CalendarIdentifierWebFilter
) {
    @Bean
    fun gameSessionRoutes(gameSessionHandler: GameSessionHandler) = router {
        "/api".nest {
            "/gameSession".nest {
               /* (GET("/detail") and queryParam("gameSessionId"){true})
                        .invoke(gameSessionHandler::getGameSession)*/
                (GET("/gameSessions") and queryParam("until"){true})
                        .invoke(gameSessionHandler::getGameSessions)
            }
            (POST("/gameSession"))
                    .invoke(gameSessionHandler::createGameSession)
            (DELETE(("/gameSession"))
                    .invoke(gameSessionHandler::deleteGameSession))
        }
    }.filter(filter)
}