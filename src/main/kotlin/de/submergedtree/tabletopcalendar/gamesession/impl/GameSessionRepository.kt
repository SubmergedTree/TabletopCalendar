package de.submergedtree.tabletopcalendar.gamesession.impl

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

@Document
data class GameHostDao(
        val player: String,
        val gameIds: List<String>
)

@Document
data class GameSessionDao(
        @Id val gameSessionId: String,
        val gameSessionName: String,
        val timestamp: String,
        val playerIds: List<String>,
        val hosts: List<GameHostDao>
)

interface GameSessionRepository: ReactiveMongoRepository<GameSessionDao, String> {
    fun getGameSessionDaosByTimestampBefore(timestamp: String): Flux<GameSessionDao>
    fun getGameSessionDaosByTimestampAfter(timestamp: String): Flux<GameSessionDao>
}
