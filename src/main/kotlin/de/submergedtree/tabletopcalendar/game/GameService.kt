package de.submergedtree.tabletopcalendar.game

interface GameService {
    fun searchGame(name: String)
    fun getGame(gameId: String)
}