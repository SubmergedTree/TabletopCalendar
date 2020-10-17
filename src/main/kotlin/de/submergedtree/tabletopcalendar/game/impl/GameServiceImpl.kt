package de.submergedtree.tabletopcalendar.game.impl

import de.submergedtree.tabletopcalendar.game.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import kotlin.streams.toList


@Service
class GameServiceImpl(@Qualifier("Cached") private val gameAttributeProviders: List<GameAttributeProvider>,
                      private val decoderService: GameSearchObjectDecoderService) : GameService {

    override fun searchGame(query: String, sources: List<String>): Flux<SearchGameResponse> {
        if (!isQueryLongEnough(query))
            return Flux.error(FaultyQuery(query))

        return Flux.fromIterable(filterSources(sources))
                .flatMap { it.search(query) }
                .map {
                    val searchKey = it.encodeBase64()
                    toSearchGameResponse(it, searchKey)
                }
    }

    override fun getGame(gameId: String): Mono<DetailGame> =
        decoderService.decode(gameId)
                .switchIfEmpty { Mono.error(UnknownGameSearchId(gameId)) }
                .zipWhen { getProviderOf(it)?.getAttributes(it) }
                .switchIfEmpty { Mono.error(UnknownProvider()) } // Does that work despite zipWhen returns a tuple ?
                .map {
                    val gso = it.t1
                    val attributeMap = it.t2
                    DetailGame(gameId, gso.name, attributeMap)
                }

    override fun validateGameKey(gameKey: String): Mono<Boolean> =
        getGame(gameKey)
                .map { true }
                .switchIfEmpty(Mono.just(false))

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

    private fun getProviderOf(gso: GameSearchObject): GameAttributeProvider? =
            gameAttributeProviders.firstOrNull { it.isProviderOf(gso) }
}