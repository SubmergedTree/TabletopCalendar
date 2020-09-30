package de.submergedtree.tabletopcalendar.game

import reactor.core.publisher.Mono

interface SearchObjectCacheService {
    fun cache(searchObject: GameSearchObject): Mono<String>
    fun get(searchId: String): Mono<GameSearchObject>
}