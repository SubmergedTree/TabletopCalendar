package de.submergedtree.tabletopcalendar.user.web

import de.submergedtree.tabletopcalendar.security.CalendarIdentifierWebFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class UserRouter(
        private val filter: CalendarIdentifierWebFilter
) {
    @Bean
    fun userRoutes(userHandler: UserHandler) = router {
        "/api".nest {
            "/user".nest {
                (GET("/getUser") and queryParam("userKey"){true})
                        .invoke(userHandler::getUser)
                GET("/getAll", userHandler::getAllUsersHandler)
            }
        }
    }.filter(filter)
}
