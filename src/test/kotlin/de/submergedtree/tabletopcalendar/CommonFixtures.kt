package de.submergedtree.tabletopcalendar

import de.submergedtree.tabletopcalendar.user.impl.UserDao
import reactor.core.publisher.Flux

val users = Flux.just(UserDao("123", "Bart"), UserDao("321", "Lisa"))
