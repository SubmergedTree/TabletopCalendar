package de.submergedtree.tabletopcalendar.game.impl

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import de.submergedtree.tabletopcalendar.game.GameAttributeProvider
import de.submergedtree.tabletopcalendar.game.GameSearchObject
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit


@Component
@Qualifier("Discoverable")
class BGGAttributeProviderCacheProxy(
        private val boardGameGeekGameAttributeProvider: BoardGameGeekGameAttributeProvider,
        @Value("\${game.searchCache.maxSize}") private val searchCacheMaxSize: Long,
        @Value("\${game.searchCache.expire}") private val searchCacheExpire: Long,
        @Value("\${game.attributesCache.maxSize}") private val attributesCacheMaxSize: Long,
        @Value("\${game.attributesCache.expire}") private val attributesCacheExpire: Long
) : GameAttributeProvider {

    private var searchCache: Cache<String, Array<GameSearchObject>> = Caffeine.newBuilder()
            .maximumSize(searchCacheMaxSize)
            .expireAfterWrite(searchCacheExpire, TimeUnit.HOURS)
            .build()

    private var attributesCache: Cache<String, Map<String, String>> = Caffeine.newBuilder()
            .maximumSize(attributesCacheMaxSize)
            .expireAfterAccess(attributesCacheExpire, TimeUnit.DAYS)
            .build();

    override fun search(query: String): Flux<GameSearchObject> {
    /*    val cached : GameSearchObject = searchCache.getIfPresent(query)
            ?: return boardGameGeekGameAttributeProvider.search(query)
                    .doOnNext{ searchCache.put(query, it) }
        return Flux.just(cached)*/

        val fetched =  boardGameGeekGameAttributeProvider.search(query)
        //fetched.doOnNext{it.}
        TODO()
    }

    override fun getAttributes(searchId: String): Mono<Map<String, String>> {
        TODO("Not yet implemented")
    }

    override fun isProviderOf(gameSearchObject: GameSearchObject) =
            boardGameGeekGameAttributeProvider.isProviderOf(gameSearchObject)
}