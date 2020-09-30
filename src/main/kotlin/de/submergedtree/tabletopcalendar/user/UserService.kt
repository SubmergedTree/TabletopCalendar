package de.submergedtree.tabletopcalendar.user

import de.submergedtree.tabletopcalendar.user.impl.UserDao
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserService {

    fun getAll(): Flux<UserDao>

    fun getUsername(userKey: String): Mono<String>

    fun updateUsername(userKey: String,newUserName: String) : Mono<UserDao>
}