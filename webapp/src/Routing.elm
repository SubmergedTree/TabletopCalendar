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

parseQuery: Url -> String -> Maybe String
parseQuery url queryKey =
    case url.query of
        Just query ->
            getQueryTuples query
            |> List.filter (\x -> (Tuple.first x) == queryKey)
            |> List.head
            |> Maybe.map Tuple.second
        Nothing -> Nothing


getQueryTuples: String -> (List (String, String))
getQueryTuples query =
    let
     queries = Maybe.map (String.split "&") (Just query)
     --String.split "?" url
     --       |> List.tail
     --       |> Maybe.andThen List.head
     --       |> Maybe.map (String.split "&")
    in
    case queries of
        Just a ->
            List.map queryStringToTuple a
            |> List.filter isJust
            |> List.map (mapJustToValue ("", ""))
        Nothing -> []


queryStringToTuple: String -> Maybe (String, String)
queryStringToTuple queryStr =
    let
        queryList = String.split "=" queryStr
        key = List.head queryList
        value = List.reverse queryList |> List.head
    in
    if (Maybe.map2 (\x y -> x == y) key value) == Just False then
        Maybe.map2 create2Tuple key value
    else
        Nothing

create2Tuple: String -> String -> (String, String)
create2Tuple x y = (x, y)

isJust: Maybe a -> Bool
isJust x =
    case x of
        Just a -> True
        Nothing -> False

mapJustToValue: a -> Maybe a -> a
mapJustToValue default x =
    case x of
        Just val -> val
        Nothing -> default
