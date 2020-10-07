package de.submergedtree.tabletopcalendar.game

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class WrongGameSearchObjectTypeProvided(type: String): Throwable("cannot cast GameSearchObject to $type")

interface GameAttributeProvider {
    fun search(query: String): Flux<GameSearchObject>
    fun getAttributes(gameSearchObject: GameSearchObject): Mono<Map<String, String>>
    fun isProviderOf(gameSearchObject: GameSearchObject) : Boolean
    val provider: String
}
