package com.deguru.dangroa.user

import com.deguru.dangroa.dtos.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Service

@Service
class UserService{

    fun getAllUser(): List<User.UserDTO> {
        return User.UsersTable
            .selectAll()
            .map { User.UserDTO(it) }
    }


    fun findUserById(id: String): User.UserDTO? {
        return User.UsersTable.selectAll()
            .where(User.UsersTable.id.eq(id))
        .singleOrNull()?.let { User.UserDTO(it) }
    }

    fun findUserByUserIndex(index: Long): User.UserDTO? {
        return User.UsersTable.selectAll()
            .where(User.UsersTable.userIndex.eq(index))
            .singleOrNull()?.let { User.UserDTO(it) }
    }


}