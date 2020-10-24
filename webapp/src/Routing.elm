module Routing exposing (..)

import Url exposing (Url)
import Url.Parser exposing (..)

type Route
    = NotFound
    | Overview

parseUrl: Url -> Route
parseUrl url =
    case parse matchRoute url of
        Just route -> route
        Nothing -> NotFound

matchRoute: Parser (Route -> a) a
matchRoute =
    oneOf
        [ map Overview top
        , map Overview (s "overview")]

