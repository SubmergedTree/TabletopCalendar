module Detail exposing (..)

import Html exposing (Html, br, div, h3, text)
import Html.Attributes exposing (class)
import Http
import Json.Decode as Decode exposing (Decoder, map3)
import Json.Decode.Extra as Decode
import Types exposing (Game, GameAttributes)

type alias Model =
    { foo: String
    , gameFetchState: GameFetchState
    }

type GameFetchState
    = Failure
   | Loading
   | Success Game

type Msg =
    FetchGame
    | GotGame (Result Http.Error Game)

init: ( Model, Cmd Msg )
init = ({foo = "", gameFetchState = Loading}, getGame)


view: Model -> Html Msg
view model =
    case model.gameFetchState of
        Failure -> renderCard "Error" <| div [] [(text "Failed to get from server")]
        Loading -> h3 [] [ text "Get game from server" ]
        Success game -> h3 [] [ text game.name ]

renderCard heading field  =
    div [class "mui-row"] [
        div [class "mui-col-sm-10 mui-col-sm-offset-1"] [
            br [] []
            , br [] []
            , div [class "mui--text-dark-secondary mui--text-body2"] [text heading]
            , div [class "mui-divider"] []
            , br [] []
            , field
        ]
    ]

update: Msg -> String -> Model -> ( Model, Cmd Msg )
update msg _ model =
    case msg of
        FetchGame ->
            (setGameLoading model, Cmd.none)
        GotGame result ->
            case result of
                Ok game ->
                  (setGameSuccess model game, Cmd.none)
                Err error ->
                    (setGameFailure model, Cmd.none)

setGameSuccess: Model -> Game -> Model
setGameSuccess model game = {model | gameFetchState = Success game }

setGameFailure: Model -> Model
setGameFailure model = {model | gameFetchState = Failure }

setGameLoading: Model -> Model
setGameLoading model = {model | gameFetchState = Loading }

getGame: Cmd Msg
getGame =
    Http.request
        { method = "GET"
        , headers = []
        , url = "http://localhost:8080/api/game/detail?gameId=eyJuYW1lIjoiQ29zbWljIEF0dGFjayIsImJnZ0lkIjoiMjU2NTMiLCJ5ZWFyUHVibGlzaGVkIjoiMjAwNiIsInByb3ZpZGVyS2V5IjoiQm9hcmRHYW1lR2VlayJ9&calendarIdentifier=ABC"
        , body = Http.emptyBody
        , expect = Http.expectJson GotGame gameDecoder
        , timeout = Nothing
        , tracker = Nothing
        }


gameAttributesDecoder: Decoder GameAttributes
gameAttributesDecoder =
    Decode.succeed GameAttributes
        |> Decode.andMap (Decode.field "imageUrl" Decode.string |> Decode.maybe)
        |> Decode.andMap (Decode.field "minPlayer" Decode.int |> Decode.maybe)
        |> Decode.andMap (Decode.field "maxPlayer" Decode.int |> Decode.maybe)
        |> Decode.andMap (Decode.field "playingTime" Decode.int |> Decode.maybe)
        |> Decode.andMap (Decode.field "description" Decode.string |> Decode.maybe)
        |> Decode.andMap (Decode.field "source" Decode.string |> Decode.maybe)
        |> Decode.andMap (Decode.field "yearPublished" Decode.string |> Decode.maybe)
        |> Decode.andMap (Decode.field "age" Decode.string |> Decode.maybe)


gameDecoder: Decoder Game
gameDecoder =
    map3 Game
        (Decode.field "gameId" Decode.string)
        (Decode.field "name" Decode.string)
        (Decode.field "attributes" gameAttributesDecoder)
