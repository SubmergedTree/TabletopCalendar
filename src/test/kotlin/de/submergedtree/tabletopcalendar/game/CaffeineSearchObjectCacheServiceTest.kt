package de.submergedtree.tabletopcalendar.game

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import de.submergedtree.tabletopcalendar.bggGameSearchObjects
import de.submergedtree.tabletopcalendar.game.impl.CaffeineSearchObjectCacheService
import de.submergedtree.tabletopcalendar.game.impl.SearchObjectCache
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import reactor.test.StepVerifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CaffeineSearchObjectCacheServiceTest {

    lateinit var cache: SearchObjectCache
    lateinit var cacheService: SearchObjectCacheService

    @BeforeAll
    fun setUp() {
        cache = mock()
        cacheService = CaffeineSearchObjectCacheService(cache)
    }

    @Test
    fun `cache single object`() {
        val gso1 = bggGameSearchObjects[0]
        val keyMono = cacheService.cache(gso1)

        StepVerifier.create(keyMono)
                .expectNextMatches{ it == gso1.hashCode().toString() }
                .verifyComplete()
    }


    @Test
    fun `cache hit`() {
        val gso1 = bggGameSearchObjects[0]
        val hash = gso1.hashCode().toString()
        println(gso1.hashCode().toString())
        whenever(cache.getIfPresent(hash)).thenReturn(gso1)
        val gsoMono = cacheService.get(hash)
        StepVerifier.create(gsoMono)
                .expectNext(gso1)
                .verifyComplete()
    }

    @Test
    fun `cache not found`() {
        val gso1 = bggGameSearchObjects[0]
        val hash = gso1.hashCode().toString()
        whenever(cache.getIfPresent(hash)).thenReturn(null)
        val gsoMono = cacheService.get(hash)
        StepVerifier.create(gsoMono)
                .verifyComplete()
    }

}