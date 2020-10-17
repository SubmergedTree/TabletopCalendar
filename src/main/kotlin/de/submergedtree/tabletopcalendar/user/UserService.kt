package de.submergedtree.tabletopcalendar.user

import de.submergedtree.tabletopcalendar.user.impl.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class UnknownUser(user: String): Throwable("User: $user is unknown")

interface UserService {

    fun createUser(userName: String): Mono<User>

    fun deleteUser(userKey: String): Mono<Void>

    fun getUser(userKey: String): Mono<User>

    fun getAll(): Flux<User>

    fun updateUsername(userKey: String,newUserName: String) : Mono<User>

    fun validateUserKey(userKey: String): Mono<Boolean>
}