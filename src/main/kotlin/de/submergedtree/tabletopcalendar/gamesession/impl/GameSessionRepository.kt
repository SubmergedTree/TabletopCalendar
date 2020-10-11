package de.submergedtree.tabletopcalendar.gamesession.impl

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

data class GameMongo(
        val userId: String,
        val gameSearchKey: String
)

@Document(value = "GameSession")
data class GameSessionMongo(
        @Id val gameSessionId: String,
        val gameSessionName: String,
        val timestamp: String,
        val userIds: List<String>,
        val hosts: List<GameMongo>
)

interface GameSessionRepository: ReactiveMongoRepository<GameSessionMongo, String> {
    fun getGameSessionByTimestampAfter(timestamp: String): Flux<GameSessionMongo>
    fun deleteGameSessionByTimestampBefore(timestamp: String): Flux<GameSessionMongo>
}
