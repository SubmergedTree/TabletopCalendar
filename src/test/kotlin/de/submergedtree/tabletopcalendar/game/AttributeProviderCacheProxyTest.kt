package de.submergedtree.tabletopcalendar.game

import com.github.benmanes.caffeine.cache.Cache
import com.nhaarman.mockito_kotlin.*
import de.submergedtree.tabletopcalendar.bggGameSearchObjects
import de.submergedtree.tabletopcalendar.bggGameSearchObjectsFlux
import de.submergedtree.tabletopcalendar.game.impl.AttributeProviderCacheProxy
import de.submergedtree.tabletopcalendar.game.impl.BGGSearchObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AttributeProviderCacheProxyTest {

    lateinit var gameAttributeProviderMock: GameAttributeProvider
    lateinit var searchCacheMock: Cache<String, Array<GameSearchObject>>
    lateinit var attributesCacheMock: Cache<String, Map<String, String>>
    lateinit var attributeProviderCacheProxy: AttributeProviderCacheProxy

    val gso = BGGSearchObject("game1", "123", "2000")

    @BeforeAll
    fun setUp() {
        gameAttributeProviderMock = mock()
        searchCacheMock = mock()
        attributesCacheMock = mock()
        attributeProviderCacheProxy = AttributeProviderCacheProxy(gameAttributeProviderMock,
                searchCacheMock,
                attributesCacheMock)
    }

    @AfterEach
    fun afterEach() {
        reset(gameAttributeProviderMock)
        reset(searchCacheMock)
        reset(attributesCacheMock)
    }

    @Test
    fun `is Provider of test subject`() {
        whenever(gameAttributeProviderMock.isProviderOf(gso)).thenReturn(Mono.just(true))
        val res = attributeProviderCacheProxy.isProviderOf(gso)

        StepVerifier.create(res)
                .expectNext(true)
                .verifyComplete()
    }

    @Test
    fun `is not Provider of test subject`() {
        whenever(gameAttributeProviderMock.isProviderOf(gso)).thenReturn(Mono.just(false))
        val res = attributeProviderCacheProxy.isProviderOf(gso)

        StepVerifier.create(res)
                .expectNext(false)
                .verifyComplete()
    }

    @Test
    fun `search and write into cache`() {
        whenever(gameAttributeProviderMock.search("test")).thenReturn(bggGameSearchObjectsFlux)
        whenever(searchCacheMock.getIfPresent("test")).thenReturn(null)

        StepVerifier.create(attributeProviderCacheProxy.search("test"))
                .expectNext(bggGameSearchObjects[0])
                .expectNext(bggGameSearchObjects[1])
                .verifyComplete()

        verify(searchCacheMock, times(1)).put("test", bggGameSearchObjects)
        verify(searchCacheMock, times(1)).getIfPresent("test")
        verifyNoMoreInteractions(searchCacheMock)
    }

    @Test
    fun `search and read from cache`() {
        whenever(searchCacheMock.getIfPresent("test")).thenReturn(bggGameSearchObjects)

        StepVerifier.create(attributeProviderCacheProxy.search("test"))
                .expectNext(bggGameSearchObjects[0])
                .expectNext(bggGameSearchObjects[1])
                .verifyComplete()

        verify(searchCacheMock, times(1)).getIfPresent("test")
        verifyNoMoreInteractions(searchCacheMock)
        verifyNoMoreInteractions(gameAttributeProviderMock)
    }

}