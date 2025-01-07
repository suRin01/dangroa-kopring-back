package com.deguru.dangroa.dtos

import com.nimbusds.jwt.JWTClaimsSet
import jakarta.annotation.Nullable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import java.util.*


class User {
    object UsersTable: Table("users") {
        val userIndex = long("userIndex").uniqueIndex().autoIncrement()
        val password = varchar("password", 255)
        val email = varchar("email", 255)
        val name = varchar("name", 255)
        val id = varchar("id", 255)
        val userStatus = long("userStatus")
        val description = varchar("description", 255)
        val enabled = bool("enabled")
    }

    data class SingUpUserDTO(
        @field:NotNull
        @field:NotBlank
        val id: String,
        @field:NotNull
        @field:NotBlank
        val name: String,
        @field:NotNull
        @field:NotBlank
        @field:Email
        val email: String,
        @field:NotNull
        @field:NotBlank
        val password: String,
        @field:Nullable
        val description: String? = null,
    )

    data class UserDTO(
        val userIndex: Long,
        val id: String,
        val name: String,
        val password: String){
        constructor(queryRow: ResultRow) : this(
            queryRow[UsersTable.userIndex],
            queryRow[UsersTable.id],
            queryRow[UsersTable.name],
            queryRow[UsersTable.password],
        )

        fun toAccessClaimSet(): JWTClaimsSet {
            return JWTClaimsSet.Builder()
                .claim("idx", this.userIndex)
                .claim("id", this.id)
                .expirationTime(Date(Date().time + 8 * 60 * 60 * 1000 )) //8 hour
                .build()
        }
        fun toAccessClaimSetWithRoles(hasRoles: List<Role.RoleDTO>): JWTClaimsSet {
            return JWTClaimsSet.Builder()
                .claim("idx", this.userIndex)
                .claim("id", this.id)
                .claim("roles", hasRoles.map { it.roleCode }.toTypedArray().joinToString("|"))
                .expirationTime(Date(Date().time + 2 * 60 * 60 * 1000 )) //2 hour
                .build()
        }
    }


}