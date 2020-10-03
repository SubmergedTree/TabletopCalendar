package de.submergedtree.tabletopcalendar.game.impl

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import de.submergedtree.tabletopcalendar.game.GameSearchObject
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

typealias SearchCache = Cache<String, Array<GameSearchObject>>
typealias AttributesCache = Cache<String, Map<String, String>>
typealias SearchObjectCache = Cache<String, GameSearchObject>

@Configuration
class CacheConfiguration(
        @Value("\${game.searchCache.maxSize}") private val searchCacheMaxSize: Long,
        @Value("\${game.searchCache.expire}") private val searchCacheExpire: Long,
        @Value("\${game.attributesCache.maxSize}") private val attributesCacheMaxSize: Long,
        @Value("\${game.attributesCache.expire}") private val attributesCacheExpire: Long,
        @Value("\${game.searchObject.maxSize}") private val searchObjectCacheMaxSize: Long,
        @Value("\${game.searchObject.expire}") private val searchObjectCacheExpire: Long
) {

    @Bean
    @Qualifier("SearchCache")
    fun searchCache(): Cache<String, Array<GameSearchObject>> =
            Caffeine.newBuilder()
                    .maximumSize(searchCacheMaxSize)
                    .expireAfterWrite(searchCacheExpire, TimeUnit.HOURS)
                    .build()

    @Bean
    @Qualifier("AttributesCache")
    fun attributesCache(): Cache<String, Map<String, String>> =
            Caffeine.newBuilder()
                    .maximumSize(attributesCacheMaxSize)
                    .expireAfterAccess(attributesCacheExpire, TimeUnit.DAYS)
                    .build()

    @Bean
    @Qualifier("SearchObjectCache")
    fun searchObjectCache(): SearchObjectCache =
            Caffeine.newBuilder()
                    .maximumSize(1000)
                    .expireAfterWrite(5, TimeUnit.MINUTES)
                    .build()
}