package de.submergedtree.tabletopcalendar.game.impl

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import de.submergedtree.tabletopcalendar.game.GameAttributeProvider
import de.submergedtree.tabletopcalendar.game.GameSearchObject
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.collections.HashMap

@Component
class BoardGameGeekGameAttributeProvider(
        @Qualifier("xmlWebClient") private val webClient: WebClient
) : GameAttributeProvider {

    val baseUrl = "http://www.boardgamegeek.com/xmlapi"

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class BGGBoardgameSearchResponse(
            var objectid: String = "",
            var name: String = "",
            var yearpublished: String = "")

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class BGGDetailResponse(
            var yearpublished: String = "N/A",
            var minplayer: String = "N/A",
            var maxplayer: String = "N/A",
            var playingtime: String = "N/A",
            var age: String = "N/A",
            var description: String = "N/A",
            var image: String = "N/A")

    override fun search(query: String): Flux<GameSearchObject> =
            webClient.get()
                    .uri("$baseUrl/search?search=$query")
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .map { convertXmlString2DataObject(it, Array<BGGBoardgameSearchResponse>::class.java) }
                    .flatMapMany { Flux.fromArray(it) }
                    .filter { it.name.isNotBlank() }
                    .map {
                        BGGSearchObject(it.name,
                                it.objectid,
                                it.yearpublished)
                    }

    override fun getAttributes(searchId: String): Mono<Map<String, String>> =
            webClient.get()
                    .uri("$baseUrl/boardgame/$searchId")
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .map { convertXmlString2DataObject(it, BGGDetailResponse::class.java) }
                    .map {
                        val map = HashMap<String, String>()
                        map["yearPublished"] = it.yearpublished
                        map["minPlayer"] = it.minplayer
                        map["maxPlayer"] = it.maxplayer
                        map["playingTime"] = it.playingtime
                        map["age"] = it.age
                        map["description"] = it.description
                        map["imageUrl"] = it.image
                        map
                    }

    override fun isProviderOf(gameSearchObject: GameSearchObject) =
            Mono.just(gameSearchObject.providerKey == "BoardGameGeek")

    private fun <T> convertXmlString2DataObject(xmlString: String, cls: Class<T>): T {
        val xmlMapper = XmlMapper()
        return xmlMapper.readValue(xmlString, cls)
    }
}