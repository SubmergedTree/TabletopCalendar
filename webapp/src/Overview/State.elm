module Overview.State exposing (..)

import Overview.Rest exposing (getGameSession)
import Overview.Types exposing (GameSessions(..), OverviewModel, OverviewMsg(..), setGameSession)

init: (OverviewModel, OverviewMsg)
init = ({gameSessions = Loading}, FetchSessions)

update : String -> OverviewMsg -> OverviewModel -> ( OverviewModel, Cmd OverviewMsg )
update token msg model =
    case msg of
        FetchSessions -> (setGameSession Loading, getGameSession token)


subscriptions : OverviewModel -> Sub OverviewMsg
subscriptions model =
  Sub.none
