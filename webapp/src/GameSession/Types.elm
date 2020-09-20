module GameSession.Types exposing (Game, GameSession, GameSessionMsg)

import Http

type alias Game =
    { name: String
    , pictureUrl: Maybe String
    , minPlayer: Maybe Int
    , maxPlayer: Maybe Int
    , playingTime: Maybe Int
    , handbookUrl: Maybe String
    , description: Maybe String
    }

type alias GameSession =
    { games: List Game
    , gameHosts: List String
    , beginTimestamp: String
    , attendees: List String
    , hint: String
    }

type GameSessionMsg = FetchGameDescription
    | GotGameDescription (Result Http.Error String)
    | FetchHint
    | GotHint (Result Http.Error String)
