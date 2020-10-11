package de.submergedtree.tabletopcalendar.security

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFilterFunction
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class CalendarIdentifierWebFilter: HandlerFilterFunction<ServerResponse, ServerResponse> {
    override fun filter(request: ServerRequest, next: HandlerFunction<ServerResponse>): Mono<ServerResponse> {
        val identifier = request.queryParam("calendarIdentifier")
        if (identifier.isPresent && identifier.get() == "ABC") {
            return next.handle(request)
        }
        return ServerResponse.status(HttpStatus.FORBIDDEN).build()
    }
}