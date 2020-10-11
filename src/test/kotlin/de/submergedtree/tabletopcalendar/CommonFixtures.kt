package de.submergedtree.tabletopcalendar

import de.submergedtree.tabletopcalendar.game.GameSearchObject
import de.submergedtree.tabletopcalendar.game.SearchGameResponse
import de.submergedtree.tabletopcalendar.game.impl.BGGSearchObject
import de.submergedtree.tabletopcalendar.user.impl.User
import reactor.core.publisher.Flux

val usersFlux = Flux.just(User("123", "Bart"),
        User("321", "Lisa"))

val bggGameSearchObjectsFlux: Flux<GameSearchObject> = Flux.just(BGGSearchObject("game1", "123", "2000"),
        BGGSearchObject("game2", "456", "2005"))

val bggGameSearchObjects: Array<GameSearchObject> = arrayOf(BGGSearchObject("game1", "123", "2000"),
    BGGSearchObject("game2", "456", "2005"))

val searchGameResponses: Array<SearchGameResponse> = arrayOf(
        SearchGameResponse("game1","2000", "BoardGameGeek", "eyJuYW1lIjoiZ2FtZTEiLCJiZ2dJZCI6IjEyMyIsInllYXJQdWJsaXNoZWQiOiIyMDAwIiwicHJvdmlkZXJLZXkiOiJCb2FyZEdhbWVHZWVrIn0="),
        SearchGameResponse("game2","2005", "BoardGameGeek",  "eyJuYW1lIjoiZ2FtZTIiLCJiZ2dJZCI6IjQ1NiIsInllYXJQdWJsaXNoZWQiOiIyMDA1IiwicHJvdmlkZXJLZXkiOiJCb2FyZEdhbWVHZWVrIn0="))

val searchGameResponsesFlux: Flux<SearchGameResponse> = Flux.just(
        SearchGameResponse("game1","2000", "BoardGameGeek",  "eyJuYW1lIjoiZ2FtZTEiLCJiZ2dJZCI6IjEyMyIsInllYXJQdWJsaXNoZWQiOiIyMDAwIiwicHJvdmlkZXJLZXkiOiJCb2FyZEdhbWVHZWVrIn0="),
        SearchGameResponse("game2","2005", "BoardGameGeek",  "eyJuYW1lIjoiZ2FtZTIiLCJiZ2dJZCI6IjQ1NiIsInllYXJQdWJsaXNoZWQiOiIyMDA1IiwicHJvdmlkZXJLZXkiOiJCb2FyZEdhbWVHZWVrIn0="))