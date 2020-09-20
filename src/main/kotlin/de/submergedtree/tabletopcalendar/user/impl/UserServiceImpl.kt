package de.submergedtree.tabletopcalendar.user.impl

import de.submergedtree.tabletopcalendar.user.UserService
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserServiceImpl() : UserService {
    override fun getUsername(userKey: Mono<String>): Mono<String> {
        return  Mono.just("")
    }
}