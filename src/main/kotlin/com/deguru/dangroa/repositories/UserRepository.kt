package com.deguru.dangroa.repositories

import com.deguru.dangroa.dtos.UserDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class UserRepository {

    fun findUserByUserIndex(userId: Long): List<UserDTO.UserAuthentication> {
        val queryResult = transaction {
            UserDTO.Users.selectAll()
                .where { UserDTO.Users.userIndex eq userId }
                .map {
                    UserDTO.UserAuthentication(it)

                }
        }

        return queryResult;
    }


    fun findUserById(id: String): List<UserDTO.UserAuthentication> {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;", driver = "org.h2.Driver")
        val queryResult = transaction {
            UserDTO.Users.selectAll()
                .where { UserDTO.Users.id eq id }
                .map {
                    UserDTO.UserAuthentication(it)

                }
        }



        return queryResult;
    }
}