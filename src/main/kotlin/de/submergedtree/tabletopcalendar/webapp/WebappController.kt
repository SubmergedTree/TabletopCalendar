package de.submergedtree.tabletopcalendar.webapp

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

@Controller
class WebappController {

    @RequestMapping("/")
    fun index(): Mono<String> {
        return Mono.just("index")
    }

}