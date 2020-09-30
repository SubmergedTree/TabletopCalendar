package de.submergedtree.tabletopcalendar.user.impl

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

@Document
data class UserDao(@Id val userKey: String, val userName: String)

interface UserRepository : ReactiveMongoRepository<UserDao, String>
