package de.submergedtree.tabletopcalendar

import de.submergedtree.tabletopcalendar.gamesession.impl.GameMongo
import de.submergedtree.tabletopcalendar.gamesession.impl.GameSessionMongo
import de.submergedtree.tabletopcalendar.gamesession.impl.GameSessionRepository
import de.submergedtree.tabletopcalendar.user.impl.User
import de.submergedtree.tabletopcalendar.user.impl.UserRepository
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux


@Component
@org.springframework.context.annotation.Profile("demo")
class SampleDataInitializer(
        private val repo : UserRepository,
        private val gsRepo: GameSessionRepository
) : ApplicationListener<ApplicationReadyEvent>, Logging {

    private val gameSearchKey = "eyJuYW1lIjoiQ29zbWljIEVuY291bnRlciIsImJnZ0lkIjoiMTUiLCJ5ZWFyUHVibGlzaGVkIjoiMTk3NyIsInByb3ZpZGVyS2V5IjoiQm9hcmRHYW1lR2VlayJ9"
    private val gameSession1 = GameSessionMongo("A",
        "sessionA",
        "1589969410",
            listOf("12632173", "54354353"),
    listOf(GameMongo("370706342", gameSearchKey)))
// 1 res: 1577873410
// 0 res: 1607595010

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        repo
                .deleteAll()
        //        .flatMap { gsRepo.deleteAll() }
                .thenMany(
                        Flux
                                .just("A", "B", "C", "D")
                                .zipWithIterable(listOf("12632173", "54354353", "370706342", "98694236"))
                                .map { User(it.t2, "${it.t1}@email.com") }
                                .flatMap(repo::save)
                )/*.thenMany(
                        Flux.just(gameSession1)
                                .flatMap(gsRepo::save)
                )*/
                .thenMany(repo.findAll())
                .subscribe { profile -> logger.info("saving " + profile.toString()) }


    }


}