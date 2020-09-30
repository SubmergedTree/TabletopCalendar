package de.submergedtree.tabletopcalendar.user.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class UserRouter {
    @Bean
    fun userRoutes(userHandler: UserHandler) = router {
        "/api".nest {
            "/user".nest {
                GET("/username", userHandler::getUsernameHandler)
                GET("/getAll", userHandler::getAllUsersHandler)
                (POST("/changeUsername") and queryParam("newUsername") {true})
                        .invoke(userHandler::updateUsername)
            }
        }
    }
}