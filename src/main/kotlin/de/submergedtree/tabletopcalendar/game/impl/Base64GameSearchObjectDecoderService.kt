package de.submergedtree.tabletopcalendar.game.impl

import com.google.gson.Gson
import com.google.gson.JsonParseException
import de.submergedtree.tabletopcalendar.game.GSODecoder
import de.submergedtree.tabletopcalendar.game.GameSearchObject
import de.submergedtree.tabletopcalendar.game.GameSearchObjectDecoderService
import de.submergedtree.tabletopcalendar.game.MalformedEncodedGameSearchObject
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.*

@Service
class Base64GameSearchObjectDecoderService(
        private val decoders: List<GSODecoder>
): GameSearchObjectDecoderService {

    private data class OnlyGSOProviderKey(val providerKey: String)

    override fun decode(str: String): Mono<GameSearchObject> {
        try {
            val json = String(Base64.getDecoder().decode(str))
            val providerKey = Gson().fromJson(json, OnlyGSOProviderKey::class.java).providerKey
            return Flux.fromIterable(decoders)
                    .filter{ it.providerKey == providerKey }
                    .next()
                    .switchIfEmpty { Mono.error(MalformedEncodedGameSearchObject()) }
                    .map { it.gsoDecoderFun(json) }
                    .onErrorResume { Mono.error(MalformedEncodedGameSearchObject()) }
        } catch (e: Exception) {
            return Mono.error(MalformedEncodedGameSearchObject())
        }
    }
}