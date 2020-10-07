package de.submergedtree.tabletopcalendar.game.impl

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import de.submergedtree.tabletopcalendar.game.GameAttributeProvider
import de.submergedtree.tabletopcalendar.game.GameSearchObject
import de.submergedtree.tabletopcalendar.game.WrongGameSearchObjectTypeProvided
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import kotlin.collections.HashMap

@Component
class BoardGameGeekGameAttributeProvider(
        @Qualifier("xmlWebClient") private val webClient: WebClient
) : GameAttributeProvider, Logging {

    val baseUrl = "http://www.boardgamegeek.com/xmlapi"

    override val provider = "BoardGameGeek"

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class BGGBoardgameSearchResponse(
            var objectid: String = "",
            var name: String = "",
            var yearpublished: String = "")

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class BGGDetailResponse(
            var yearpublished: String = "N/A",
            var minplayers: String = "N/A",
            var maxplayers: String = "N/A",
            var playingtime: String = "N/A",
            var age: String = "N/A",
            var description: String = "N/A",
            var image: String = "N/A")

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class BGGDetailResponseTopLevel(var boardgame: BGGDetailResponse = BGGDetailResponse())

    override fun search(query: String): Flux<GameSearchObject> =
            webClient.get()
                    .uri("$baseUrl/search?search=$query")
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .map { convertXmlString2DataObject(it, Array<BGGBoardgameSearchResponse>::class.java) }
                    .flatMapMany { Flux.fromArray(it) }
                    .filter { it.name.isNotBlank() }
                    .doOnComplete{ logger.info("Query Board Game Geek with query: $query")}
                    .map {
                        BGGSearchObject(it.name,
                                it.objectid,
                                it.yearpublished)
                    }

    override fun getAttributes(gameSearchObject: GameSearchObject): Mono<Map<String, String>> =
            Mono.just(gameSearchObject)
                    .flatMap {
                        if (isProviderOf(it))
                             Mono.just(it as BGGSearchObject)
                        else
                            Mono.error(WrongGameSearchObjectTypeProvided("BGGSearchObject"))
                    }.flatMap { getAttributesRequest(it.bggId) }

    private fun getAttributesRequest(searchId: String) =
            webClient.get()
                    .uri("$baseUrl/boardgame/$searchId")
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .map {
                        convertXmlString2DataObject(it, BGGDetailResponseTopLevel::class.java)
                    }
                    .map {
                        val map = HashMap<String, String>()
                        val boardgame = it.boardgame
                        map["source"] = provider
                        map["yearPublished"] = boardgame.yearpublished
                        map["minPlayer"] = boardgame.minplayers
                        map["maxPlayer"] = boardgame.maxplayers
                        map["playingTime"] = boardgame.playingtime
                        map["age"] = boardgame.age
                        map["description"] = boardgame.description
                        map["imageUrl"] = boardgame.image
                        map
                    }

    override fun isProviderOf(gameSearchObject: GameSearchObject) =
            gameSearchObject.providerKey == provider

    private fun <T> convertXmlString2DataObject(xmlString: String, cls: Class<T>): T {
        val xmlMapper = XmlMapper()
        return xmlMapper.readValue(xmlString, cls)
    }
}