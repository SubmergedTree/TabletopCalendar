package de.submergedtree.tabletopcalendar.game.impl

import com.github.benmanes.caffeine.cache.Cache
import de.submergedtree.tabletopcalendar.game.GameAttributeProvider
import de.submergedtree.tabletopcalendar.game.GameSearchObject
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

typealias CacheProxyDecorator = (GameAttributeProvider) -> AttributeProviderCacheProxy

@Configuration
class CacheProxyDecoratorConfiguration  {

    @Bean
    @Qualifier("CacheProxyDecorator")
    fun cacheProxyDecorator(
            @Qualifier("SearchCache") searchCache: Cache<String, Array<GameSearchObject>>,
            @Qualifier("AttributesCache") attributesCache: Cache<String, Map<String, String>>
    ): CacheProxyDecorator =
            { toDecorate: GameAttributeProvider -> AttributeProviderCacheProxy(toDecorate, searchCache, attributesCache) }

    @Bean
    @Qualifier("Cached")
    fun bggAttributeProvider(
            @Qualifier("CacheProxyDecorator") cpd: CacheProxyDecorator,
            bggAttributeProvider: BoardGameGeekGameAttributeProvider
    ): GameAttributeProvider = cpd(bggAttributeProvider)

}