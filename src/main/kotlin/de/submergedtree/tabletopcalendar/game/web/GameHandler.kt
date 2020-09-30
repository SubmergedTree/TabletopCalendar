package de.submergedtree.tabletopcalendar.game.web

import de.submergedtree.tabletopcalendar.game.GameService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class GameHandler(private val gameService: GameService) {
    fun search(req: ServerRequest): Mono<ServerResponse> {
        TODO()
    }

    fun getGameDetail(req: ServerRequest): Mono<ServerResponse> {
        TODO()
    }
}