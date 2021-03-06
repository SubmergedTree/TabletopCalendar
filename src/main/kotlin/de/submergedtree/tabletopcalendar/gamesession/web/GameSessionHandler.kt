package de.submergedtree.tabletopcalendar.gamesession.web

import de.submergedtree.tabletopcalendar.game.MalformedEncodedGameSearchObject
import de.submergedtree.tabletopcalendar.gamesession.CreateGameSession
import de.submergedtree.tabletopcalendar.gamesession.GameSessionService
import de.submergedtree.tabletopcalendar.gamesession.UpdateGameSession
import de.submergedtree.tabletopcalendar.web.ErrorBody
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.lang.IllegalArgumentException

data class SessionIdResponse(val gameSessionId: String)

@Component
class GameSessionHandler(
        private val gameSessionService: GameSessionService
): Logging {

    fun getGameSession(req: ServerRequest): Mono<ServerResponse> {
        TODO("Not needed")
    }
    
    fun getGameSessions(req: ServerRequest): Mono<ServerResponse> {
        val until = req.queryParam("until")
        return Mono.justOrEmpty(until)
                .flatMapMany { gameSessionService.getSessions(it) }
                .collectList()
                .flatMap {
                    ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(it.toTypedArray())
                }.onErrorResume {
                    logger.error(it)
                    when(it!!) {
                        is MalformedEncodedGameSearchObject -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .bodyValue(ErrorBody("Corrupted game data"))
                        else -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                    }
                }
    }

    fun createGameSession(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<CreateGameSession>()
                .flatMap { gameSessionService.createSession(it) }
                .flatMap {
                    ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(SessionIdResponse(it))
                }.onErrorResume {
                    logger.error(it)
                    when(it!!) {
                        is MalformedEncodedGameSearchObject -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .bodyValue(ErrorBody("GameKey is invalid"))
                        is ServerWebInputException -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                               .bodyValue(ErrorBody("Invalid body"))
                        else -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                    }
                }

    fun updateGameSession(req: ServerRequest): Mono<ServerResponse> =
            req.bodyToMono<UpdateGameSession>()
                    .flatMap{gameSessionService.updateSession(it)}
                    .flatMap {
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(SessionIdResponse(it))
                    }.onErrorResume {
                        logger.error(it)
                        when(it!!) {
                            is MalformedEncodedGameSearchObject -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                    .bodyValue(ErrorBody("GameKey is invalid"))
                            is ServerWebInputException -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                    .bodyValue(ErrorBody("Invalid body"))
                            else -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                        }
                    }

    fun deleteGameSession(req: ServerRequest): Mono<ServerResponse> {
        val until = req.queryParam("gameSessionId")
        return Mono.justOrEmpty(until)
                .flatMap{gameSessionService.deleteSession(it)}
                .flatMap {
                    ServerResponse.ok().build()
                            //.contentType(MediaType.APPLICATION_JSON)
                            //.bodyValue(DeleteSessionResponse("K/A"))
                }.switchIfEmpty { ServerResponse.status(HttpStatus.NOT_FOUND).build() }
                .onErrorResume {
                    logger.error(it)
                    ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                }

    }

}
