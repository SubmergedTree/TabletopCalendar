module Types exposing (..)

import Http


type alias Game =
    { name: String
    , thumbnailUrl: Maybe String
    , minPlayer: Maybe Int
    , maxPlayer: Maybe Int
    , playingTime: Maybe Int
    , handbookUrl: Maybe String
    }

type alias GameSession =
    { games: List Game
    , gameHosts: List String
    , beginTimestamp: String
    , attendees: List String
    , hint: String
    }

type alias Timespan =
    { from: String
    , to: String
    }

type GameSessions =
     Failure
    |Loading
    |Success (List GameSession)

type alias Model =
    { bearerToken: String
    , userName: String
    , gameSessions: GameSessions
    , gameSessionsFetchedFrom: Timespan
    }

type FetchGameSessionsMsg = FetchGameSessions | GotGameSessions (Result Http.Error String)

