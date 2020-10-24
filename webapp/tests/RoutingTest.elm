module RoutingTest exposing (..)

import Expect exposing (Expectation)
import Fuzz exposing (Fuzzer, int, list, string)
import Routing exposing (getQueryTuples)
import Test exposing (..)
import Url exposing (Protocol(..))


suite : Test
suite =
    describe "Routing"
    [test "extract "
        (\_ ->
            let
                testUrl = Url.fromString "http://www.foo.com/?foo=bar"
                parsed = Maybe.andThen (\x -> Routing.parseQuery x "foo") testUrl
            in
            case parsed of
                Just a -> Expect.equal "bar" a
                Nothing -> Expect.fail "found no matching query param"
        ),
    test "getQueryTuples"
        (\_ ->
            let
                tuples = getQueryTuples "foo=bar&bazz=fuzz"
            in
            tuples |> Expect.equalLists [("foo", "bar"), ("bazz", "fuzz")]
        )
    ]
