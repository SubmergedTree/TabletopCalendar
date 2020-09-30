package de.submergedtree.tabletopcalendar.user

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import de.submergedtree.tabletopcalendar.user.impl.UserDao
import de.submergedtree.tabletopcalendar.user.impl.UserRepository
import de.submergedtree.tabletopcalendar.user.impl.UserServiceImpl
import de.submergedtree.tabletopcalendar.users
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.function.Predicate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplTest {

    @Mock
    lateinit var userRepository: UserRepository

    private lateinit var userService: UserService

    @BeforeAll
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userService = UserServiceImpl(userRepository)
    }

    @Test
    fun getAllTest() {
        Mockito.`when`(userRepository.findAll())
                .thenReturn(users)
        val match = Predicate<UserDao> { { u: UserDao -> users.any { saved: UserDao -> saved == u } }(it)
                .block()?: false }

        StepVerifier.create(userService.getAll())
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete()
    }

    @Test
    fun getUsernameTest_UserDoesNotExist() {
        Mockito.`when`(userRepository.findById("123"))
                .thenReturn(Mono.empty())
        Mockito.`when`(userRepository.save(UserDao("123", "defaultName")))
                .thenReturn(Mono.just(UserDao("123", "defaultName")))

        StepVerifier.create(userService.getUsername("123"))
                .expectNext("defaultName")
                .verifyComplete()

        verify(userRepository, times(1)).findById("123")
        verify(userRepository, times(1)).save(UserDao("123", "defaultName"))
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun getUsername_UserDoesExist() {
        Mockito.`when`(userRepository.findById("123"))
                .thenReturn(Mono.just(UserDao("123", "Bart")))

        StepVerifier.create(userService.getUsername("123"))
                .expectNext("Bart")
                .verifyComplete()

        verify(userRepository, times(1)).findById("123")
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun updateUsername_UserDoesExist() {
        // TODO
    }

    @Test
    fun updateUsername_UserDoesNotExist() {
        // TODO
    }

}