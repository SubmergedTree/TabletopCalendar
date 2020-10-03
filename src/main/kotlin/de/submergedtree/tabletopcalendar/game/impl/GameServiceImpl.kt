package de.submergedtree.tabletopcalendar.game.impl

import de.submergedtree.tabletopcalendar.game.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.streams.toList

@Service
class GameServiceImpl(@Qualifier("Cached") private val gameAttributeProviders: List<GameAttributeProvider>,
                      private val searchObjectCacheService: SearchObjectCacheService) : GameService {

    override fun searchGame(query: String, sources: List<String>): Flux<SearchGameResponse> {
        if (!isQueryLongEnough(query))
            return Flux.error(FaultyQuery(query))

        return Flux.fromIterable(filterSources(sources))
                .flatMap { it.search(query) }
                .map { toSearchGameResponse(it, "123") }
    }

    override fun getGame(gameId: String) {
        TODO("Not yet implemented")
        // lookup cache
        // search provider
        // load attributes and return Detail Game Object.
    }

    private fun isQueryLongEnough(query: String) =
            query.length >= 3

    private fun filterSources(sources: List<String>): List<GameAttributeProvider> {
        if (sources.isNullOrEmpty())
            return gameAttributeProviders
        return gameAttributeProviders.stream()
                .filter{ sources.contains(it.provider)}
                .toList()
    }

    private fun toSearchGameResponse(gso: GameSearchObject, searchKey: String) =
            SearchGameResponse(gso.name, gso.yearPublished, gso.providerKey, searchKey)
}