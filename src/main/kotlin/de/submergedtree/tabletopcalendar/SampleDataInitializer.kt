package de.submergedtree.tabletopcalendar

import de.submergedtree.tabletopcalendar.user.impl.UserDao
import de.submergedtree.tabletopcalendar.user.impl.UserRepository
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.*


@Component
@org.springframework.context.annotation.Profile("demo")
class SampleDataInitializer(private val repo : UserRepository) : ApplicationListener<ApplicationReadyEvent>, Logging {



    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        repo
                .deleteAll()
                .thenMany(
                        Flux
                                .just("A", "B", "C", "D")
                                .map { name: String -> UserDao(UUID.randomUUID().toString(), "$name@email.com") }
                                .flatMap(repo::save)
                )
                .thenMany(repo.findAll())
                .subscribe { profile -> logger.info("saving " + profile.toString()) }

    }


}