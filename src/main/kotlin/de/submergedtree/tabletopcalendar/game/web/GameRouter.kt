package de.submergedtree.tabletopcalendar.game.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class GameRouter {
    @Bean
    fun gameRoutes(gameHandler: GameHandler) = router {
        "/api".nest {
            "/game".nest {
                (GET("/search") and queryParam("query"){true}//{it.length >= 3}
                        and queryParam("sources"){true})
                        .invoke(gameHandler::search)
                GET("/detail", gameHandler::getGameDetail)
            }
        }
    }
}