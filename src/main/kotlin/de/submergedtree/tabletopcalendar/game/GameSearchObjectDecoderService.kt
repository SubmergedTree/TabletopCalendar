package de.submergedtree.tabletopcalendar.game

import reactor.core.publisher.Mono

class MalformedEncodedGameSearchObject: Throwable("Given encoded GameSearchObject is malformed")

interface GameSearchObjectDecoderService {
    fun decode(str: String): Mono<GameSearchObject>
}