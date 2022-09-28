package tech.fdiez.flowfullybackend.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import tech.fdiez.flowfullybackend.data.UserData

@Repository
interface UserRepository : MongoRepository<UserData, String> {

    fun findByUsername(username: String): UserData?

    fun existsByUsername(username: String): Boolean
}