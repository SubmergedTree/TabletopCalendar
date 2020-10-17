package de.submergedtree.tabletopcalendar.user

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import de.submergedtree.tabletopcalendar.user.impl.User
import de.submergedtree.tabletopcalendar.user.web.UserHandler
import de.submergedtree.tabletopcalendar.user.web.UserRouter
import de.submergedtree.tabletopcalendar.user.web.UsernameData
import de.submergedtree.tabletopcalendar.usersFlux
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono


@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRouterTest {

    @MockBean
    lateinit var userHandler: UserHandler

    private lateinit var router : RouterFunction<ServerResponse>
    private lateinit var client : WebTestClient

    @BeforeAll
    fun setUp() {
        router = UserRouter(mock()).userRoutes(userHandler)
        client = WebTestClient.bindToRouterFunction(router).build()
    }

    @Test
    fun getAllTest() {
        Mockito.`when`(userHandler.getAllUsersHandler(any()))
                .thenReturn(ServerResponse
                        .ok()
                        .body(usersFlux, User::class.java))

        client.get()
                .uri("/api/user/getAll")
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.[0].userKey").isEqualTo("123")
                .jsonPath("$.[0].userName").isEqualTo("Bart")
                .jsonPath("$.[1].userKey").isEqualTo("321")
                .jsonPath("$.[1].userName").isEqualTo("Lisa");
    }

    @Test
    fun getUserTest() {
        Mockito.`when`(userHandler.getUser(any()))
                .thenReturn(ServerResponse
                        .ok()
                        .body(Mono.just(User("123", "Bart")), UsernameData::class.java))

        client.get()
                .uri("/api/user/getUser")
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.userKey").isEqualTo("123")
                .jsonPath("$.userName").isEqualTo("Bart")
    }
/*
    @Test
    @WithMockUser
    fun changeUsernameTest() {
        Mockito.`when`(userHandler.updateUsername(any()))
                .thenReturn(ServerResponse
                        .ok()
                        .body(Mono.just(UsernameData("Bart")), UsernameData::class.java))

        client.post()
                .uri("/api/user/changeUsername?newUsername=Bart")
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.userName").isEqualTo("Bart")
    }

    @Test
    @WithMockUser
    fun changeUsernameTestMissingUsername() {
        Mockito.`when`(userHandler.updateUsername(any()))
                .thenReturn(ServerResponse
                        .ok()
                        .body(Mono.just(UsernameData("Bart")), UsernameData::class.java))

        client.post()
                .uri("/api/user/changeUsername")
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().is4xxClientError
    }
*/
}