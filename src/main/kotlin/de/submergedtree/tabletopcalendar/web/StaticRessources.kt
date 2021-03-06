package de.submergedtree.tabletopcalendar.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class StaticRessources {
    @Bean
    fun staticResourceRouter(): RouterFunction<ServerResponse?>? {
        return RouterFunctions.resources("/**", ClassPathResource("static/built/"))
    }
}