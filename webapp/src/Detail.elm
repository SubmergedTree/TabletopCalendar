module Detail exposing (..)

import Html exposing (Html, h3, text)
type alias Model =
    { foo: String
    }

type Msg =
    DoSth

init: ( Model, Cmd Msg )
init = ({foo = ""}, Cmd.none)


view: Model -> Html Msg
view model =
    h3 [] [ text "Detail" ]

update: Msg -> Model -> ( Model, Cmd Msg )
update msg model = (model, Cmd.none)
