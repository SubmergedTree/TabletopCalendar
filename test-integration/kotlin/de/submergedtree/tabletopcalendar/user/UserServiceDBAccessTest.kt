package de.submergedtree.tabletopcalendar.user

import de.submergedtree.tabletopcalendar.user.impl.UserDao
import de.submergedtree.tabletopcalendar.user.impl.UserRepository
import de.submergedtree.tabletopcalendar.user.impl.UserServiceImpl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*
import java.util.function.Predicate


//TODO: use embedded mongo db for integration tests
//TODO: delete all entries after each test.

@DataMongoTest
@Import(UserServiceImpl::class)
class UserServiceDBAccessTest @Autowired constructor(private val userSvc: UserServiceImpl, private val userRepo: UserRepository) {

    @Test
    fun getAllTest() {
        val savedUsers = userRepo.saveAll(Flux.just(UserDao("123", "Bart"),
                UserDao("321", "Lisa")))
        val composite = userSvc.getAll().thenMany(savedUsers)
        val match = Predicate<UserDao> { {u: UserDao -> savedUsers.any { saved: UserDao -> saved == u } }(it)
                .block()?: false }
        StepVerifier
                .create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete()
    }

    @Test
    fun getUsernameTest() {
        val username = userRepo.saveAll(Flux.just(UserDao("123", "Bart"),
                UserDao("321", "Lisa")))
                .then(userSvc.getUsername("123"))
        StepVerifier
                .create(username)
                .expectNextMatches { u -> u == "Bart"}
                .verifyComplete()
    }

    // TODO
    @Test
    fun updateUsernameTest() {
        val updated = userRepo.saveAll(Flux.just(UserDao("621", "Homer"),
                UserDao("321", "Lisa")))
                .then(userSvc.updateUsername("621", "Marge"))
                .thenMany(userSvc.getAll())
/*
        StepVerifier
                .create(updated)
                .expectNextMatches { u -> u.userName == "Marge"}
                .expectNextMatches { u -> u.userName == "Lisa"}
                .verifyComplete()*/
    }

}