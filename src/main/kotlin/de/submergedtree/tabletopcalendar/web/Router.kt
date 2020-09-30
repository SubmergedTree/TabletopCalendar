package de.submergedtree.tabletopcalendar.web

import de.submergedtree.tabletopcalendar.gamesession.impl.GameSessionController
import de.submergedtree.tabletopcalendar.user.web.UserHandler
import org.springframework.web.reactive.function.server.router

//@Configuration
class Router(private val gameSessionController: GameSessionController,
             private val userHandler: UserHandler) {

   // @Bean
    fun apiRouter() = router {
        //GET("/") { ok().render("index") }
        "/api".nest {
          //  accept(MediaType.APPLICATION_JSON).nest {
          /*      "/gameSession".nest {
                    GET("/detail", gameSessionController::getGameSessionHandler)
                    GET("/all", gameSessionController::getAllGameSessionsHandler)
                    DELETE("/remove")
                    POST("/update")
                }*/
                "/user".nest {
                    GET("/username", userHandler::getUsernameHandler)
                    GET("/getAll", userHandler::getAllUsersHandler)
                    (POST("/changeUsername") and queryParam("username") {true})
                            .invoke(userHandler::getUsernameHandler)
                }
/*                "/game".nest {
                    GET("/search")
                }*/
            }
      //  }
    }
}