package de.submergedtree.tabletopcalendar.gamesession.impl

import de.submergedtree.tabletopcalendar.gamesession.IdentifierService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UUIDIdentifierService: IdentifierService {
    override fun generate(): String =
            UUID.randomUUID().toString()
}