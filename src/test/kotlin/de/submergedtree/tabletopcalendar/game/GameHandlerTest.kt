package de.submergedtree.tabletopcalendar.game

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import de.submergedtree.tabletopcalendar.game.web.GameHandler
import de.submergedtree.tabletopcalendar.searchGameResponses
import de.submergedtree.tabletopcalendar.searchGameResponsesFlux
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*
import kotlin.collections.ArrayList

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameHandlerTest {

    lateinit var gameHandler: GameHandler
    lateinit var gameService: GameService
    lateinit var serverRequest: ServerRequest

    @BeforeAll
    fun setUp() {
        gameService = mock()
        serverRequest = mock()
        gameHandler = GameHandler(gameService)
    }

    @AfterEach
    fun afterEach() {
        reset(gameService)
        reset(serverRequest)
    }

    @Test
    fun `handle search`() {
        whenever(serverRequest.queryParam("query")).thenReturn(Optional.of("ABC"))
        whenever(serverRequest.queryParam("sources")).thenReturn(Optional.empty())
        whenever(gameService.searchGame("ABC", ArrayList())).thenReturn(searchGameResponsesFlux)

        val res = gameHandler.search(serverRequest)

        StepVerifier.create(res)
                .expectNextMatches{it.statusCode() == HttpStatus.OK}
                .verifyComplete()
    }

    @Test
    fun `handle to short search param`() {
        whenever(serverRequest.queryParam("query")).thenReturn(Optional.of("AB"))
        whenever(serverRequest.queryParam("sources")).thenReturn(Optional.empty())
        whenever(gameService.searchGame("AB", ArrayList())).thenReturn(Flux.error(FaultyQuery("AB")))

        val res = gameHandler.search(serverRequest)

        StepVerifier.create(res)
                .expectNextMatches{it.statusCode() == HttpStatus.BAD_REQUEST}
                .verifyComplete()
    }
}