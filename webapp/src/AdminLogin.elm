port module AdminLogin exposing (..)

import Html exposing (Attribute, Html, button, div, text)
import Html.Events exposing (onClick)
import Json.Encode exposing (Value)
import Json.Decode as Decode

type alias Model =
    { isLoggedIn: Bool
    , loginAdminMessage: String
    , logoutAdminMessage: String
    , adminBearerToken: String
    , error: Maybe String}

type Msg
    = AddBearerToken String
    | SendToJs String
    | CreateError String

init: String -> String -> ( Model, Cmd Msg )
init loginMsg logoutMsg =
    ({isLoggedIn = False
     , loginAdminMessage = loginMsg
     , logoutAdminMessage = logoutMsg
     , adminBearerToken = ""
     , error = Nothing}, Cmd.none)

port toJs : String -> Cmd msg
port toElm : (Value -> msg) -> Sub msg


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        AddBearerToken str ->
            ( { model | adminBearerToken = str }, Cmd.none )

        SendToJs str ->
            ( model, toJs str )

        CreateError str ->
            ( { model | error = Just str }, Cmd.none )


view : Model -> Html Msg
view model =
    case model.adminBearerToken /= "" of
        True ->
            div []
                [
                 button
                    [ onClick (SendToJs model.logoutAdminMessage) ]
                    [ text "Logout" ]
                 ]
        False ->
              div []
                    [
                     button
                        [ onClick (SendToJs model.loginAdminMessage) ]
                        [ text "To admin console" ]
                     ]


decodeValue : Value -> Msg
decodeValue x =
    let
        result =
            Decode.decodeValue Decode.string x
    in
        case result of
            Ok string ->
                AddBearerToken string

            Err _ ->
                CreateError "Login failed"
