package de.submergedtree.tabletopcalendar.game.web

import de.submergedtree.tabletopcalendar.security.CalendarIdentifierWebFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class GameRouter(
        private val filter: CalendarIdentifierWebFilter
) {
    @Bean
    fun gameRoutes(gameHandler: GameHandler) = router {
        "/api".nest {
            "/game".nest {
                (GET("/search") and queryParam("query"){true}//{it.length >= 3}
                        and queryParam("sources"){true})
                        .invoke(gameHandler::search)
                (GET("/detail") and queryParam("gameId"){true})
                        .invoke(gameHandler::getGameDetail)
            }
        }
    }.filter(filter)
}