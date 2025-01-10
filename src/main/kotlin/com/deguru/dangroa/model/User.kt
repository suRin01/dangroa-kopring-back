package com.deguru.dangroa.model

import com.deguru.dangroa.constants.UserStatus
import com.fasterxml.jackson.annotation.JsonFormat
import com.nimbusds.jwt.JWTClaimsSet
import jakarta.annotation.Nullable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.springframework.format.annotation.DateTimeFormat
import java.util.*


class User {
    object UsersTable: IdTable<Long>("users") {
        override val id = long("userIndex").uniqueIndex().autoIncrement().entityId()
        val password = varchar("password", 255)
        val email = varchar("email", 255)
        val name = varchar("name", 255)
        val loginId = varchar("loginId", 255)
        val userStatus = long("userStatus").default(UserStatus.ACTIVE.status)
        val description = varchar("description", 255).nullable()
        val isDeleted = bool("isDeleted").default(false)
    }

    data class SignUpUserDTO(
        @field:NotNull
        @field:NotBlank
        val loginId: String,
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

    data class UserUpdateDTO(
        val userIndex: Long,
        val email: String,
        val description: String?,
    )

    data class UserDTO(
        val userIndex: Long,
        val loginId: String,
        val name: String,
        val password: String,
        val isDeleted: Boolean,
        val userStatus: UserStatus,){
        constructor(queryRow: ResultRow) : this(
            queryRow[UsersTable.id].value,
            queryRow[UsersTable.loginId],
            queryRow[UsersTable.name],
            queryRow[UsersTable.password],
            queryRow[UsersTable.isDeleted],
            UserStatus.getByValue(queryRow[UsersTable.userStatus]),
        )

        fun toAccessClaimSet(): JWTClaimsSet {
            return JWTClaimsSet.Builder()
                .claim("idx", this.userIndex)
                .claim("id", this.loginId)
                .expirationTime(Date(Date().time + 8 * 60 * 60 * 1000 )) //8 hour
                .build()
        }
        fun toAccessClaimSetWithRoles(hasRoles: List<Role.RoleDTO>): JWTClaimsSet {
            return JWTClaimsSet.Builder()
                .claim("idx", this.userIndex)
                .claim("id", this.loginId)
                .claim("roles", hasRoles.map { it.roleCode }.toTypedArray().joinToString("|"))
                .expirationTime(Date(Date().time + 2 * 60 * 60 * 1000 )) //2 hour
                .build()
        }
    }

    data class UserSearchParam(
        @Nullable
        val name: String?,
        @Nullable
        val loginId: String?,
        @Email(regexp = "^[a-zA-Z0-9].[a-zA-Z0-9\\._%\\+\\-]{0,63}@[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z]{2,30}\$")
        @Nullable
        val email: String?,
        @JsonFormat(pattern = "yyyy-MM-dd")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Nullable
        val registeredDate: Date?,
        @JsonFormat(pattern = "yyyy-MM-dd")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Nullable
        val lastLoginDate: Date?,
        @Nullable
        val isDeleted: Boolean?,
        @Nullable
        val userStatus: Long?,

        )


}