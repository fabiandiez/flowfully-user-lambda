package tech.fdiez.flowfullybackend.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import tech.fdiez.flowfullybackend.data.UserData

@Repository
interface UserRepository : CrudRepository<UserData, String> {
    fun findByUsername(username: String): UserData?
}