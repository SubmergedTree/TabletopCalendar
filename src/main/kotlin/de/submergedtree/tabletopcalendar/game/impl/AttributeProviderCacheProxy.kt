package de.submergedtree.tabletopcalendar.game.impl

import com.github.benmanes.caffeine.cache.Cache
import de.submergedtree.tabletopcalendar.game.GameAttributeProvider
import de.submergedtree.tabletopcalendar.game.GameSearchObject
import org.apache.logging.log4j.kotlin.Logging
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class AttributeProviderCacheProxy(
        private val toProxy: GameAttributeProvider,
        private val searchCache: Cache<String, Array<GameSearchObject>>,
        private val attributesCache: Cache<String, Map<String, String>>
) : GameAttributeProvider, Logging {

    override fun search(query: String): Flux<GameSearchObject> =
            Mono.just(query)
                    .flatMapMany { key ->
                        val cacheContains = searchCache.getIfPresent(key)
                        if (cacheContains == null) {
                            toProxy.search(key)
                                    .collectList()
                                    .doOnSuccess {
                                        val asArray = it.toTypedArray()
                                        searchCache.put(key, asArray)
                                        logger.info("Write to cache with key: $query")
                                    }.flatMapMany { Flux.fromIterable(it) }
                        } else {
                            logger.info("Cache hit for: $query")
                            Flux.fromArray(cacheContains)
                        }
                    }


    // TODO use cache
    override fun getAttributes(gameSearchObject: GameSearchObject): Mono<Map<String, String>> =
        toProxy.getAttributes(gameSearchObject)

    override fun isProviderOf(gameSearchObject: GameSearchObject) =
            toProxy.isProviderOf(gameSearchObject)

    override val provider = toProxy.provider
}