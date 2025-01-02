package com.deguru.dangroa.dtos

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table


class UserDTO {
    object Users: Table("users") {
        val userIndex = long("userIndex").uniqueIndex().autoIncrement();
        val userName = varchar("userName", 255);
        val password = varchar("password", 255);
        val email = varchar("email", 255);
        val name = varchar("name", 255);
        val id = varchar("id", 255);
        val userStatus = long("userStatus");
        val description = varchar("description", 255);
        val enabled = bool("enabled");
    }

    data class UserAuthentication(
        val id: String,
        val userName: String,
        val password: String){
        constructor(queryRow: ResultRow) : this(
            queryRow[Users.id],
            queryRow[Users.userName],
            queryRow[Users.password]
        )

    }
}