package de.submergedtree.tabletopcalendar.user.impl

import de.submergedtree.tabletopcalendar.user.UserService
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Component
class UserController(private val userService : UserService) {

    fun getUsernameHandler(req: ServerRequest) = ServerResponse.ok()
            .body(userService.getUsername(req.principal().map { it.name }), String::class.java)


}