package de.submergedtree.tabletopcalendar.user

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import de.submergedtree.tabletopcalendar.user.impl.User
import de.submergedtree.tabletopcalendar.user.impl.UserRepository
import de.submergedtree.tabletopcalendar.user.impl.UserServiceImpl
import de.submergedtree.tabletopcalendar.usersFlux
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
                .thenReturn(usersFlux)
        val match = Predicate<User> { { u: User -> usersFlux.any { saved: User -> saved == u } }(it)
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
        Mockito.`when`(userRepository.save(User("123", "defaultName")))
                .thenReturn(Mono.just(User("123", "defaultName")))

        StepVerifier.create(userService.getUsernameOrCreate("123"))
                .expectNext("defaultName")
                .verifyComplete()

        verify(userRepository, times(1)).findById("123")
        verify(userRepository, times(1)).save(User("123", "defaultName"))
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun getUsername_UserDoesExist() {
        Mockito.`when`(userRepository.findById("123"))
                .thenReturn(Mono.just(User("123", "Bart")))

        StepVerifier.create(userService.getUsernameOrCreate("123"))
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

    @Test
    fun `validation fails because no user is found for key`() {
        Mockito.`when`(userRepository.findById("123"))
                .thenReturn(Mono.empty())

        val res = userService.validateUserKey("123")

        StepVerifier.create(res)
                .expectNext(false)
                .verifyComplete()
    }

    @Test
    fun `validation passes`() {
        Mockito.`when`(userRepository.findById("123"))
                .thenReturn(Mono.just(User("123", "Bart")))

        val res = userService.validateUserKey("123")

        StepVerifier.create(res)
                .expectNext(true)
                .verifyComplete()
    }


}