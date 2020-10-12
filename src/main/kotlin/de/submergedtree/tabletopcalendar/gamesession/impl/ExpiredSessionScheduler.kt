package de.submergedtree.tabletopcalendar.gamesession.impl

import de.submergedtree.tabletopcalendar.gamesession.GameSessionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers

@Component
class ExpiredSessionScheduler(
        private val gameSessionService: GameSessionService,
        @Value("\${gameSession.daysUntilExpire}") private val daysUntilExpire: Int
){
    @Scheduled(cron = "0 1 * * *")
    fun expireSessionTask() {
        gameSessionService.deleteExpiredSession(daysUntilExpire)
                .subscribeOn(Schedulers.parallel()).subscribe()
    }
}
