package de.submergedtree.tabletopcalendar.user.impl

import de.submergedtree.tabletopcalendar.user.UnknownUser
import de.submergedtree.tabletopcalendar.user.UserService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService, Logging {

    override fun getUser(userKey: String): Mono<User> =
            userRepository.findById(userKey)

    override fun getAll() =
            this.userRepository.findAll()

    override fun getUsernameOrCreate(userKey: String) =
         userRepository.findById(userKey)
                .map { userDao ->
                        val name = userDao.userName
                        logger.info("found user with name: $name for key: $userKey")
                        name
                }.switchIfEmpty(createUser(userKey, "defaultName").map { it.userName })

    override fun updateUsername(userKey: String, newUserName: String) =
            userRepository
                .findById(userKey)
                .map {  User(it.userKey, newUserName) }
                .flatMap{  u -> userRepository.save(u) }
                .switchIfEmpty(createUser(userKey, newUserName))

    override fun validateUserKey(userKey: String): Mono<String> =
        getUser(userKey)
                .map { userKey }
                .switchIfEmpty(Mono.error(UnknownUser(userKey)))
               // .onErrorResume { Mono.error(UnknownUser(userKey)) }

    private fun createUser(userKey: String, userName: String) =
            Mono.fromCallable {
                logger.info("create new user for userKey: $userKey with userName: $userName")
                User(userKey, userName)}
                .flatMap{ userDao -> userRepository.save(userDao)}
}