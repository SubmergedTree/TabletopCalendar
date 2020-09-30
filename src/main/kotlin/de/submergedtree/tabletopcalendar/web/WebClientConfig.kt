package de.submergedtree.tabletopcalendar.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.http.codec.xml.Jaxb2XmlDecoder
import org.springframework.http.codec.xml.Jaxb2XmlEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient


@Configuration
class WebClientConfig(private final val webClientBuilder: WebClient.Builder) {
    @Bean
    @Qualifier("xmlWebClient")
    fun xmlWebClient(): WebClient {
        return webClientBuilder
                .clientConnector(ReactorClientHttpConnector(
                        HttpClient.create().followRedirect(true)
                        ))
              /*  .exchangeStrategies(ExchangeStrategies.builder().codecs { configurer ->
                        configurer.defaultCodecs().jaxb2Encoder(Jaxb2XmlEncoder());
                        configurer.defaultCodecs().jaxb2Decoder(Jaxb2XmlDecoder());
               //     configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(XmlMapper(), MediaType.TEXT_XML))
               //     configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(XmlMapper(), MediaType.TEXT_XML))

                }.build())*/
                .build()
    }
}