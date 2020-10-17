package de.submergedtree.tabletopcalendar.user.impl

import de.submergedtree.tabletopcalendar.gamesession.IdentifierService
import de.submergedtree.tabletopcalendar.user.UnknownUser
import de.submergedtree.tabletopcalendar.user.UserService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(
        private val userRepository: UserRepository,
        private val identifierService: IdentifierService
) : UserService, Logging {

    override fun getUser(userKey: String): Mono<User> =
            userRepository.findById(userKey)

    override fun getAll() =
            this.userRepository.findAll()

    override fun createUser(userName: String) =
            Mono.fromCallable {
                val userKey = identifierService.generate()
                logger.info("create new user for userKey: $userKey with userName: $userName")
                User(userKey, userName)}
                    .flatMap{ userDao -> userRepository.save(userDao) }

    override fun deleteUser(userKey: String): Mono<Void> =
            userRepository.findById(userKey)
                    .flatMap { userRepository.deleteById(it.userKey) }

    override fun updateUsername(userKey: String, newUserName: String) =
            userRepository
                .findById(userKey)
                .map {  User(it.userKey, newUserName) }
                .flatMap{  u -> userRepository.save(u) }

    override fun validateUserKey(userKey: String): Mono<Boolean> =
        getUser(userKey)
                .map { true }
                .switchIfEmpty(Mono.just(false))
}