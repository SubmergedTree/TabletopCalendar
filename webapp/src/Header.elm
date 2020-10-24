module Header exposing (..)

import Html exposing (Html, a, br, div, text)
import Html.Attributes exposing (class, href, id)

view loginButton model = div [] [
        div [id "sidebar" ]
        [
           div[class "mui--text-light mui--text-display1 mui--align-vertical"][
            text "Tabletop Calendar"
            , br [] []
            , loginButton model
           ]
        ]
    ]
