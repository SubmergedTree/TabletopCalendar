module Overview.Types exposing (..)

import Http
import GameSession.Types exposing (GameSession)

type GameSessions
    = Failure
    |Loading
    |Success (List GameSession)

type alias OverviewModel = {gameSessions: GameSessions}

type OverviewMsg = FetchSessions
    | GotSessions (Result Http.Error (List GameSession))

setGameSession: OverviewModel -> GameSessions -> OverviewModel
setGameSession model gameSessions = { model | gameSessions = gameSessions}
