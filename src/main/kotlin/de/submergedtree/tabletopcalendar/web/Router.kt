package de.submergedtree.tabletopcalendar.web

import de.submergedtree.tabletopcalendar.gamesession.impl.GameSessionController
import de.submergedtree.tabletopcalendar.user.impl.UserController
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class Router(private val gameSessionController: GameSessionController,
             private val userController: UserController) {

    fun routes() = router {
        GET("/") { ok().render("index") }
        "api/".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                "gameSession".nest {
                    GET("/", gameSessionController::getGameSessionHandler)
                }
                "user".nest {
                    GET("/username", userController::getUsernameHandler)
                }
                "game".nest {

                }
            }
        }
    }
}