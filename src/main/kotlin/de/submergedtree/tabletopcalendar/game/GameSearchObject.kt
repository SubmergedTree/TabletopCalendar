package de.submergedtree.tabletopcalendar.game

typealias GSODecoderFun = (json: String) -> GameSearchObject

data class GSODecoder(val providerKey: String, val gsoDecoderFun: GSODecoderFun)

interface GameSearchObject {
    val name: String
    val yearPublished: String
    val providerKey: String
    fun encodeBase64(): String
}