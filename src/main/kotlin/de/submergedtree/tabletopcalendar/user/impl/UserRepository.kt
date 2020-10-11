package de.submergedtree.tabletopcalendar.user.impl

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

@Document
data class User(@Id val userKey: String, val userName: String)
// we should hide the information, that mongo is used for services
interface UserRepository: ReactiveMongoRepository<User, String>
