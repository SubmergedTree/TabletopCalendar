package de.submergedtree.tabletopcalendar.game

import com.nhaarman.mockito_kotlin.*
import de.submergedtree.tabletopcalendar.bggGameSearchObjects
import de.submergedtree.tabletopcalendar.bggGameSearchObjectsFlux
import de.submergedtree.tabletopcalendar.game.impl.GameServiceImpl
import de.submergedtree.tabletopcalendar.searchGameResponses
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameServiceImplTest {

    lateinit var gameService: GameService
    lateinit var attributeProvider1: GameAttributeProvider
    lateinit var attributeProvider2: GameAttributeProvider
    lateinit var searchObjectCacheService: SearchObjectCacheService

    @BeforeAll
    fun setUp() {
        searchObjectCacheService = mock()
        attributeProvider1 = mock()
        attributeProvider2 = mock()
        val attributeProviderList = listOf(attributeProvider1, attributeProvider2)
        gameService = GameServiceImpl(attributeProviderList, searchObjectCacheService)
    }

    @AfterEach
    fun afterEach() {
        reset(attributeProvider1)
        reset(attributeProvider2)
        reset(searchObjectCacheService)
    }

    @Test
    fun `search game where search query is to short`() {
        val res = gameService.searchGame("A", ArrayList())
        StepVerifier.create(res)
                .expectErrorMatches{ it.message == FaultyQuery("A").message}
                .verify()
    }

    @Test
    fun `search game with unknown sources`() {
        whenever(attributeProvider1.provider).thenReturn("bla")
        whenever(attributeProvider2.provider).thenReturn("blub")
        val res = gameService.searchGame("ABCDE", listOf("foo", "bar"))
        StepVerifier.create(res)
                .expectSubscription()
                .verifyComplete()
        verify(attributeProvider1, times(1)).provider
        verify(attributeProvider2, times(1)).provider
        verifyNoMoreInteractions(attributeProvider1)
        verifyNoMoreInteractions(attributeProvider2)
    }

    @Test
    fun `search game and no games are found`() {
        whenever(attributeProvider1.search("ABCD")).thenReturn(Flux.empty())
        whenever(attributeProvider2.search("ABCD")).thenReturn(Flux.empty())
        val res = gameService.searchGame("ABCD", ArrayList())
        StepVerifier.create(res)
                .expectSubscription()
                .verifyComplete()
        verify(attributeProvider1, times(1)).search("ABCD")
        verify(attributeProvider2, times(1)).search("ABCD")
        verifyNoMoreInteractions(attributeProvider1)
        verifyNoMoreInteractions(attributeProvider2)
    }

    @Test
    fun `search game and find some`() {
        whenever(attributeProvider1.search("ABCD")).thenReturn(bggGameSearchObjectsFlux)
        whenever(attributeProvider1.provider).thenReturn("BoardGameGeek")
        whenever(attributeProvider2.search("ABCD")).thenReturn(Flux.empty())
        whenever(attributeProvider2.provider).thenReturn("FooSource")
        val res = gameService.searchGame("ABCD", ArrayList())
        StepVerifier.create(res)
                .expectNext(searchGameResponses[0])
                .expectNext(searchGameResponses[1])
                .verifyComplete()

        verify(attributeProvider1, times(1)).search("ABCD")
        verify(attributeProvider2, times(1)).search("ABCD")
        verifyNoMoreInteractions(attributeProvider1)
        verifyNoMoreInteractions(attributeProvider2)
    }

}