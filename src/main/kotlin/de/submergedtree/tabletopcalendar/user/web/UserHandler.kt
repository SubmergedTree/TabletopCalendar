package de.submergedtree.tabletopcalendar.user.web

import de.submergedtree.tabletopcalendar.user.UserService
import de.submergedtree.tabletopcalendar.user.impl.User
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class UserHandler(private val userService : UserService) : Logging {

    fun getAllUsersHandler(req: ServerRequest): Mono<ServerResponse> {
        logger.info("get All request")
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getAll(), User::class.java)
    }

    fun getUsernameHandler(req: ServerRequest): Mono<ServerResponse> {
        val username = req.principal()
                        .flatMap { userService.getUsernameOrCreate(it.name) }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(username.map { UsernameData(it) }, UsernameData::class.java)
    }

    fun updateUsername(req: ServerRequest) : Mono<ServerResponse> {
         val userName = Mono.fromCallable { req.queryParam("newUsername") }
                .flatMap { optional -> optional.map{Mono.just(it)}.orElseGet{Mono.empty()} }
        val userKey = req.principal()
                .map { it.name }
        return userName.zipWith(userKey)
                .flatMap { userTuple -> userService.updateUsername(userTuple.t1, userTuple.t2) }
                .map { UsernameData(it.userName) }
                .flatMap{ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(it, UsernameData::class.java)}
    }
}