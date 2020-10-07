package de.submergedtree.tabletopcalendar.game.impl

import com.google.gson.Gson
import de.submergedtree.tabletopcalendar.game.GSODecoder
import de.submergedtree.tabletopcalendar.game.GameSearchObject
import de.submergedtree.tabletopcalendar.web.encodeObjectToJson
import de.submergedtree.tabletopcalendar.web.encodeStringBase64
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

data class BGGSearchObject(override val name: String,
                           val bggId: String,
                           override val yearPublished: String,
                           override val providerKey: String = "BoardGameGeek"): GameSearchObject {

    override fun encodeBase64(): String {
        val jsonString = encodeObjectToJson(this)
        return encodeStringBase64(jsonString)
    }
}

@Configuration
class BGGSearchObjectConfig {
    @Bean
    fun bggSearchObjectBase64Decoder(): GSODecoder =
            GSODecoder("BoardGameGeek",
                    { json: String -> Gson().fromJson(json, BGGSearchObject::class.java)})
}