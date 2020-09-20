module Overview.Rest exposing (..)

import GameSession.Types exposing (Game, GameSession)
import Http
import Http exposing (header)
import Json.Decode as Decode exposing (Decoder, field, int, map7, string)
import Overview.Types exposing (OverviewMsg(..))

getGameSession: String -> Cmd OverviewMsg
getGameSession token =
    Http.request
        { method = "GET"
        , headers = [ header "Authorization" token ]
        , url = "http://localhost:8080/api/session"
        , body = Http.emptyBody
        , expect = Http.expectJson GotSessions gameSessionDecoder
        , timeout = Nothing
        , tracker = Nothing
        }

gameDecoder: Decoder Game
gameDecoder =
    map7 Game
        (field "name" string)
        (field "thumbnailUrl" string |> Decode.maybe)
        (field "minPlayer" int |> Decode.maybe)
        (field "maxPlayer" int |> Decode.maybe)
        (field "id" string |> Decode.maybe)
        (field "id" string |> Decode.maybe)
        (field "id" string |> Decode.maybe)



gameSessionDecoder: Decoder GameSession

gameSessionDecoder : Decoder (List GameSession)
