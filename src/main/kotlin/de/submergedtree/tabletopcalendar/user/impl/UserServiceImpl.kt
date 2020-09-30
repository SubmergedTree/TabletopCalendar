package de.submergedtree.tabletopcalendar.user.impl

import de.submergedtree.tabletopcalendar.user.UserService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService, Logging {

    override fun getAll() =
            this.userRepository.findAll()

    override fun getUsername(userKey: String) =
         userRepository.findById(userKey)
                .map { userDao ->
                        val name = userDao.userName
                        logger.info("found user with name: $name for key: $userKey")
                        name
                }.switchIfEmpty(createUser(userKey, "defaultName").map { it.userName })

    override fun updateUsername(userKey: String, newUserName: String) =
            userRepository
                .findById(userKey)
                .map {  UserDao(it.userKey, newUserName) }
                .flatMap{  u -> userRepository.save(u) }
                .switchIfEmpty(createUser(userKey, newUserName))

    private fun createUser(userKey: String, userName: String) =
            Mono.fromCallable {
                logger.info("create new user for userKey: $userKey with userName: $userName")
                UserDao(userKey, userName)}
                .flatMap{ userDao -> userRepository.save(userDao)}
}