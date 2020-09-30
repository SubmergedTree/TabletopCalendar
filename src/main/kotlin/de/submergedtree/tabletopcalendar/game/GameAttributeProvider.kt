package de.submergedtree.tabletopcalendar.game

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GameAttributeProvider {
    fun search(query: String): Flux<GameSearchObject>
    fun getAttributes(searchId: String): Mono<Map<String, String>>
    fun isProviderOf(gameSearchObject: GameSearchObject) : Mono<Boolean>
}
