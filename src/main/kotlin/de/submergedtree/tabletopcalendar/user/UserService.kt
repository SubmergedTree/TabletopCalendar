package de.submergedtree.tabletopcalendar.user

import org.springframework.security.core.context.SecurityContext
import reactor.core.publisher.Mono

interface UserService {
    fun getUsername(userKey: Mono<String>): Mono<String>
}