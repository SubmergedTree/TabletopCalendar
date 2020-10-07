package de.submergedtree.tabletopcalendar.gamesession

data class GameHost(
        val player: String,
        val gameIds: List<String>
)

data class GameSession(
        val gameSessionId: String,
        val gameSessionName: String,
        val timestamp: String,
        val playerIds: List<String>,
        val hosts: List<GameHost>
)