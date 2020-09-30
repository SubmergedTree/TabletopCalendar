package de.submergedtree.tabletopcalendar.game.impl

import de.submergedtree.tabletopcalendar.game.GameSearchObject

data class BGGSearchObject(override val name: String,
                           val bggId: String,
                           override val yearPublished: String): GameSearchObject {
    override val providerKey = "BoardGameGeek"
}