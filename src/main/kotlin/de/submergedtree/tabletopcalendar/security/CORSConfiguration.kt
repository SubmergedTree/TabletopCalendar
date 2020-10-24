package de.submergedtree.tabletopcalendar.security

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer


@Configuration
class CORSConfiguration : WebFluxConfigurer{
    override fun addCorsMappings(corsRegistry: CorsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
    }
}