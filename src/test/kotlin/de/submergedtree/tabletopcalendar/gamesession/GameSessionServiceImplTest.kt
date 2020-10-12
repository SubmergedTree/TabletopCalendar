package de.submergedtree.tabletopcalendar.gamesession

import de.submergedtree.tabletopcalendar.game.GameService
import de.submergedtree.tabletopcalendar.game.UnknownGameSearchId
import de.submergedtree.tabletopcalendar.gamesession.impl.GameMongo
import de.submergedtree.tabletopcalendar.gamesession.impl.GameSessionMongo
import de.submergedtree.tabletopcalendar.gamesession.impl.GameSessionRepository
import de.submergedtree.tabletopcalendar.gamesession.impl.GameSessionServiceImpl
import de.submergedtree.tabletopcalendar.user.UserService
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameSessionServiceImplTest {

    @Mock
    lateinit var gameSessionRepository: GameSessionRepository
    @Mock
    lateinit var userService: UserService
    @Mock
    lateinit var gameService: GameService
    @Mock
    lateinit var identifierService: IdentifierService

    lateinit var gameSessionService: GameSessionService

    @BeforeAll
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        gameSessionService = GameSessionServiceImpl(gameSessionRepository,
                userService, gameService, identifierService)
    }

    @Test
    fun `create a session`() {
        val createGameSession = CreateGameSession("name", "123456",
                listOf("123","456"),
                listOf(CreatePresentedGame("host1", "gameKey1")))

        Mockito.`when`(identifierService.generate())
                .thenReturn("ABC")
        Mockito.`when`(userService.validateUserKey("123"))
                .thenReturn(Mono.just("123"))
        Mockito.`when`(userService.validateUserKey("456"))
                .thenReturn(Mono.just("456"))
        Mockito.`when`(userService.validateUserKey("host1"))
                .thenReturn(Mono.just("host1"))
        Mockito.`when`(gameService.validateGameKey("gameKey1"))
                .thenReturn(Mono.just("gameKey1"))
        Mockito.`when`(identifierService.generate())
                .thenReturn("ID1")
        val gsm = GameSessionMongo("ID1",
                createGameSession.gameSessionName,
                createGameSession.timestamp,
                createGameSession.playerIds,
                listOf(GameMongo("host1", "gameKey1")))
        Mockito.`when`(gameSessionRepository.save(gsm))
                .thenReturn(Mono.just(gsm))

        StepVerifier.create(gameSessionService.createSession(createGameSession))
                .expectNext("ID1")
                .verifyComplete()
    }

    @Test
    fun `create session fails because of unknown users`() {
        // TODO
     /*   val createGameSession = CreateGameSession("name", "123456",
                listOf("123","456"),
                listOf(CreatePresentedGame("host1", "gameKey1")))

        Mockito.`when`(identifierService.generate())
                .thenReturn("ABC")
        Mockito.`when`(userService.validateUserKey("123"))
                .thenReturn(Mono.error(UnknownGameSearchId("123")))
        Mockito.`when`(userService.validateUserKey("456"))
                .thenReturn(Mono.just("456"))
        Mockito.`when`(userService.validateUserKey("host1"))
                .thenReturn(Mono.just("host1"))
        Mockito.`when`(gameService.validateGameKey("gameKey1"))
                .thenReturn(Mono.just("gameKey1"))
        Mockito.`when`(identifierService.generate())
                .thenReturn("ID1")
        val gsm = GameSessionMongo("ID1",
                createGameSession.gameSessionName,
                createGameSession.timestamp,
                createGameSession.playerIds,
                listOf(GameMongo("host1", "gameKey1")))
        Mockito.`when`(gameSessionRepository.save(gsm))
                .thenReturn(Mono.just(gsm))

        StepVerifier.create(gameSessionService.createSession(createGameSession))
                .expectNext("ID1")
                .verifyComplete()*/
    }

    @Test
    fun `create session fails because of malformed game key`() {
        // TODO
    }

}