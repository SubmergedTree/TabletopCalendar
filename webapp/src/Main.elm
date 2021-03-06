module Main exposing (..)

import AdminLogin
import Detail

import Browser exposing (Document, UrlRequest)
import Browser.Navigation as Nav
import Header
import Html exposing (Html, div, h2, h3, text)
import Html.Attributes exposing (class, id)
import Routing as Route exposing (..)
import Url exposing (Url)

main =
  Browser.application
      { init = init
      , update = update
      , subscriptions = subscriptions
      , view = view
      , onUrlRequest = LinkClicked
      , onUrlChange = UrlChanged
      }

type alias Flags =
    { loginMessage : String
    , logoutMessage : String
    , backendUrl: String
    }

type Msg
    = AdminLoginMsg AdminLogin.Msg
    | OverviewMsg Detail.Msg -- TODO rename to OverviewMsg
    | LinkClicked UrlRequest
    | UrlChanged Url


type alias Model =
    { adminLogin: AdminLogin.Model
    , route: Route
    , navKey : Nav.Key
    , calendarIdentifier: Maybe String
    , page : Page
    , backendUrl: String
    }

type Page
    = NotFoundPage
    | Detail Detail.Model

init: Flags -> Url -> Nav.Key -> ( Model, Cmd Msg )
init flags url navKey =
    let (adminLoginModel, _) = AdminLogin.init flags.loginMessage flags.logoutMessage
        model =
            { adminLogin = adminLoginModel
            , route = Route.parseUrl url
            , calendarIdentifier = parseQuery url "calendarIdentifier"
            , navKey = navKey
            , page = NotFoundPage
            , backendUrl = flags.backendUrl
            }
    in
    initCurrentPage (model, Cmd.none)

initCurrentPage: ( Model, Cmd Msg ) -> ( Model, Cmd Msg )
initCurrentPage (model, existingCmds) =
    let
        ( currentPage, mappedPageCmds ) =
            case model.route of
                Route.NotFound ->
                    ( NotFoundPage, Cmd.none )
                Overview ->
                    let
                        ( overviewModel, overviewCmds ) =
                            Detail.init
                    in
                    ( Detail overviewModel, Cmd.map OverviewMsg overviewCmds )
    in
    ({model | page = currentPage}
    , Cmd.batch [ existingCmds, mappedPageCmds ])

subscriptions : Model -> Sub Msg
subscriptions _ =
    AdminLogin.toElm <| \toDecode -> AdminLoginMsg (AdminLogin.decodeValue toDecode)

update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case (msg, model.page) of
        ( AdminLoginMsg adminLoggingMsg, _ ) ->
            let
                (newModel, newCmd) = AdminLogin.update adminLoggingMsg model.adminLogin
            in
            ({ model | adminLogin = newModel}, Cmd.map AdminLoginMsg newCmd)

        ( LinkClicked urlRequest, _ ) ->
            case urlRequest of
                Browser.Internal url ->
                    ( model
                    , Nav.pushUrl model.navKey (Url.toString url)
                    )

                Browser.External url ->
                    ( model
                    , Nav.load url
                    )

        ( UrlChanged url, _ ) ->
            let
                newRoute =
                    Route.parseUrl url
            in
            ( { model | route = newRoute }, Cmd.none )
                |> initCurrentPage

        (OverviewMsg overviewMsg, Detail detailModel) ->
            let
                (newModel, newCmd) = Detail.update overviewMsg model.backendUrl detailModel
            in
            ({ model | page = Detail newModel}, Cmd.map OverviewMsg newCmd)

        (_, _) ->
            ( model, Cmd.none )


view : Model -> Document Msg
view model =
    {
     title = "Tabletop"
     , body = [
         Header.view AdminLogin.view model.adminLogin |> Html.map AdminLoginMsg
         , wrapIntoBody <| currentView model
      ]
    }

wrapIntoBody: (Html Msg) -> Html Msg
wrapIntoBody toWrap =
    div [id "content",class "mui-container-fluid"] [
        toWrap
    ]

currentView: Model -> Html Msg
currentView model =
    case model.page of
        NotFoundPage ->
             notFoundView
        Detail detailModel ->
            Detail.view detailModel
                |> Html.map OverviewMsg

notFoundView : Html msg
notFoundView =
    h3 [] [ text "404 - Not Found" ]
