module Types exposing (..)

type alias GameAttributes =
    { imageUrl: Maybe String
    , minPlayer: Maybe Int
    , maxPlayer: Maybe Int
    , playingTime: Maybe Int
    --, handbookUrl: Maybe String
    , description: Maybe String
    , source: Maybe String
    , yearPublished: Maybe String
    , age: Maybe String
    }

type alias Game =
   { gameId: String
   , name: String
   , attributes: GameAttributes
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