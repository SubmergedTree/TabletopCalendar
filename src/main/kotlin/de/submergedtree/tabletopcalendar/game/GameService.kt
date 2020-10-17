package de.submergedtree.tabletopcalendar.game

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

data class SearchGameResponse(val name: String,
                              val yearPublished: String,
                              val source: String,
                              val searchId: String)

class FaultyQuery(query: String): Throwable("query: $query is faulty")

class UnknownGameSearchId(gameId: String): Throwable("gameId: $gameId is unknown")

class UnknownProvider: Throwable("provider is not known for requested game")

interface GameService {
    fun searchGame(query: String, sources: List<String>): Flux<SearchGameResponse>
    fun getGame(gameId: String): Mono<DetailGame>
    fun validateGameKey(gameKey: String): Mono<Boolean>
}