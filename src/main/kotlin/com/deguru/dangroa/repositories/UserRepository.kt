package com.deguru.dangroa.repositories

import com.deguru.dangroa.dtos.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class UserRepository {
    fun findAllUsers(): List<User.UserDTO> {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;", driver = "org.h2.Driver")
        val queryResult = transaction {
            addLogger(StdOutSqlLogger)
            User.UsersTable
                .selectAll()
                .map { User.UserDTO(it) }

        }

        return queryResult
    }


    fun findSingleUserById(id: String): User.UserDTO {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;", driver = "org.h2.Driver")
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