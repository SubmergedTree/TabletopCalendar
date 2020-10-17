module Main exposing (..)

import AdminLogin

import Browser
import Html exposing (Html, div, h2, text)

main =
  Browser.element
      { init = init
      , update = update
      , subscriptions = subscriptions
      , view = view
      }

type alias Flags =
    { loginMessage : String
    , logoutMessage : String
    }

type Msg
    = AdminLoginMsg AdminLogin.Msg

type alias Model =
    { adminLogin: AdminLogin.Model
    }

init: Flags -> ( Model, Cmd Msg )
init flags =
    let (adminLoginModel, _) = AdminLogin.init flags.loginMessage flags.logoutMessage
    in
    ({adminLogin = adminLoginModel}
    , Cmd.none)

subscriptions : Model -> Sub Msg
subscriptions _ =
    AdminLogin.toElm <| \toDecode -> AdminLoginMsg (AdminLogin.decodeValue toDecode)

update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        AdminLoginMsg adminLoggingMsg ->
            let (newModel, newCmd) = AdminLogin.update adminLoggingMsg model.adminLogin
            in
            ({ model | adminLogin = newModel}, Cmd.map AdminLoginMsg newCmd)

view : Model -> Html Msg
view model =
    div []
        [
            h2 [] [text "Tabletop Calendar"]
            ,AdminLogin.view model.adminLogin |> Html.map AdminLoginMsg
        ]
