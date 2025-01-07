package com.deguru.dangroa.repositories

import com.deguru.dangroa.dtos.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class UserRepository {
    fun findAllUsers(): List<User.UserDTO> {
        val queryResult = transaction {
            addLogger(StdOutSqlLogger)
            User.UsersTable
                .selectAll()
                .map { User.UserDTO(it) }

        }

        return queryResult
    }


    fun findSingleUserById(id: String): User.UserDTO {
        val queryResult = transaction {
            addLogger(StdOutSqlLogger)
            User.UsersTable
                .selectAll()
                .where { User.UsersTable.id eq id }
                .singleOrNull()?.let{
                    User.UserDTO(it)
                }

        }
        if(queryResult == null) {
            throw NoSuchElementException("User with ID $id not found")
        }

        return queryResult
    }
}