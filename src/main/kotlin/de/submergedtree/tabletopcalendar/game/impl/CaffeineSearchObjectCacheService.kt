package de.submergedtree.tabletopcalendar.game.impl

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import de.submergedtree.tabletopcalendar.game.GameSearchObject
import de.submergedtree.tabletopcalendar.game.SearchObjectCacheService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit


// https://stackoverflow.com/questions/52787925/cache-the-result-of-a-mono-from-a-webclient-call-in-a-spring-webflux-web-applica
@Service
class CaffeineSearchObjectCacheService(@Qualifier("SearchObjectCache") val cache: SearchObjectCache): SearchObjectCacheService {

    override fun cache(searchObject: GameSearchObject): Mono<String> {
        val searchId = searchObject.hashCode().toString()
        cache.put(searchId, searchObject)
        return Mono.just(searchId)
    }

    override fun get(searchId: String): Mono<GameSearchObject> =
        Mono.justOrEmpty(cache.getIfPresent(searchId))
}