module Main exposing (..)

import Browser
import Html exposing (Html, button, div, text, h2)
import Http
import Http exposing (header)
import Json.Decode exposing (Decoder, field, string)

main =
  Browser.element
      { init = init
      , update = updateGameSession
      , subscriptions = subscriptions
      , view = view
      }

type Msg = Fetch | GotGameSession (Result Http.Error String)

type GameSession
    = Failure
    |Loading
    |Success String

type alias Model =
    { token: String
    , gameSession: GameSession
    }

subscriptions : Model -> Sub Msg
subscriptions model =
  Sub.none

init: String -> (Model, Cmd Msg)
init bearerToken = ({token = bearerToken, gameSession = Loading}, getGameSession bearerToken)

getGameSession: String -> Cmd Msg
getGameSession token =
    Http.request
        { method = "GET"
        , headers = [ header "Authorization" token ]
        , url = "http://localhost:8080/api/session"
        , body = Http.emptyBody
        , expect = Http.expectJson GotGameSession gameSessionDecoder
        , timeout = Nothing
        , tracker = Nothing
        }

gameSessionDecoder : Decoder String
gameSessionDecoder =
  field "id" string

updateGameSession: Msg -> Model -> (Model, Cmd Msg)
updateGameSession msg model =
    case msg of
        Fetch -> (setGameSession model Loading , getGameSession model.token)
        GotGameSession result ->
            case result of
                Ok id -> (setGameSession model (Success id), Cmd.none)
                Err _ -> (setGameSession model Failure, Cmd.none)

setGameSession: Model -> GameSession -> Model
setGameSession model gameSession = { model | gameSession = gameSession}

view: Model -> Html Msg
view model =
  div []
    [ h2 [] [ text "Tabletop Calendar" ]
    , viewGameSession model.gameSession
    ]

viewGameSession: GameSession -> Html Msg
viewGameSession gameSession =
  case gameSession of
    Loading -> text "Loading..."
    Failure -> text "Error"
    Success id ->  div [] [text id]

