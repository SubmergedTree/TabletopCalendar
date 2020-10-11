package de.submergedtree.tabletopcalendar.gamesession

import de.submergedtree.tabletopcalendar.game.DetailGame
import de.submergedtree.tabletopcalendar.user.impl.User


data class PresentedGame(
        val host: User,
        val game: DetailGame
)

data class DetailedGameSession(
        val gameSessionId: String,
        val gameSessionName: String,
        val timestamp: String,
        val players: List<User>,
        val presentedGame: List<PresentedGame>
)

data class CreatePresentedGame(
        val host: String,
        val gameKey: String
)

data class CreateGameSession(
        val gameSessionName: String,
        val timestamp: String,
        val playerIds: List<String>,
        val presentedGames: List<CreatePresentedGame>
)