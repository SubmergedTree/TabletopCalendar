package de.submergedtree.tabletopcalendar.game.web

import de.submergedtree.tabletopcalendar.game.FaultyQuery
import de.submergedtree.tabletopcalendar.game.GameService
import de.submergedtree.tabletopcalendar.game.SearchGameResponse
import de.submergedtree.tabletopcalendar.web.parseCommaSeparatedStringToArray
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*
import kotlin.collections.ArrayList

data class SearchBadRequest(val error: String)

@Component
class GameHandler(private val gameService: GameService): Logging {

    fun search(req: ServerRequest): Mono<ServerResponse> {
        logger.info("search game request")
        val query = req.queryParam("query")
        val sources = extractSourcesParam(req.queryParam("sources"))
        return Mono.justOrEmpty(query)
                .flatMapMany { gameService.searchGame(query = it, sources = sources) }
                .collectList()
                .flatMap {
                    ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(it.toTypedArray())
                }.onErrorResume {
                    when(it!!) {
                        is FaultyQuery -> ServerResponse.badRequest()
                                .bodyValue(SearchBadRequest("Search param must be longer then 3 chars"))
                        else -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                    }
                }
    }


    fun getGameDetail(req: ServerRequest): Mono<ServerResponse> {
        TODO()
    }

    fun extractSourcesParam(sources: Optional<String>): List<String> {
        val commaSeparated = sources.map { parseCommaSeparatedStringToArray(it) }
        if (commaSeparated.isPresent)
            return commaSeparated.get()
        return ArrayList()
    }
}