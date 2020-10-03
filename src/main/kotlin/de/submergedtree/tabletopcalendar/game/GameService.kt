package de.submergedtree.tabletopcalendar.game

import reactor.core.publisher.Flux

data class SearchGameResponse(val name: String,
                              val yearPublished: String,
                              val source: String,
                              val searchId: String)

class FaultyQuery(val query: String) : Throwable("query: $query is faulty")

interface GameService {
    fun searchGame(query: String, sources: List<String>): Flux<SearchGameResponse>
    fun getGame(gameId: String)
}