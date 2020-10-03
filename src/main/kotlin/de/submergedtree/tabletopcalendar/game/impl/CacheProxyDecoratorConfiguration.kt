package de.submergedtree.tabletopcalendar.game.impl

import com.github.benmanes.caffeine.cache.Cache
import de.submergedtree.tabletopcalendar.game.GameAttributeProvider
import de.submergedtree.tabletopcalendar.game.GameSearchObject
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

typealias CacheProxyFactory = (GameAttributeProvider) -> AttributeProviderCacheProxy

@Configuration
class CacheProxyDecoratorConfiguration  {

    @Bean
    @Qualifier("CacheProxyFactory")
    fun cacheProxyFactory(
            @Qualifier("SearchCache") searchCache: Cache<String, Array<GameSearchObject>>,
            @Qualifier("AttributesCache") attributesCache: Cache<String, Map<String, String>>
    ): CacheProxyFactory =
            { toDecorate: GameAttributeProvider -> AttributeProviderCacheProxy(toDecorate, searchCache, attributesCache) }

    @Bean
    @Qualifier("Cached")
    fun bggAttributeProvider(
            @Qualifier("CacheProxyFactory") cpf: CacheProxyFactory,
            bggAttributeProvider: BoardGameGeekGameAttributeProvider
    ): GameAttributeProvider = cpf(bggAttributeProvider)

}