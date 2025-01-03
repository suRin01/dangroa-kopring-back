package com.deguru.dangroa.dtos

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table


class User {
    object UsersTable: Table("users") {
        val userIndex = long("userIndex").uniqueIndex().autoIncrement()
        val userName = varchar("userName", 255)
        val password = varchar("password", 255)
        val email = varchar("email", 255)
        val name = varchar("name", 255)
        val id = varchar("id", 255)
        val userStatus = long("userStatus")
        val description = varchar("description", 255)
        val enabled = bool("enabled")
    }

    data class UserDTO(
        val userIndex: Long,
        val id: String,
        val userName: String,
        val password: String){
        constructor(queryRow: ResultRow) : this(
            queryRow[UsersTable.userIndex],
            queryRow[UsersTable.id],
            queryRow[UsersTable.userName],
            queryRow[UsersTable.password],
        )
    }
}