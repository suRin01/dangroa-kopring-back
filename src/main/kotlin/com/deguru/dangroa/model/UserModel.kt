package com.deguru.dangroa.model

import com.deguru.dangroa.constants.UserStatus
import com.deguru.dangroa.global.BaseEntity
import com.deguru.dangroa.global.BaseEntityClass
import com.deguru.dangroa.global.BaseLongIdTable
import com.deguru.dangroa.model.MenuModel.Menu
import com.deguru.dangroa.model.MenuModel.Menus
import com.fasterxml.jackson.annotation.JsonFormat
import com.nimbusds.jwt.JWTClaimsSet
import jakarta.annotation.Nullable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.springframework.format.annotation.DateTimeFormat
import java.util.*


class UserModel {
    object Users: BaseLongIdTable("user", "user_index") {
        val password = varchar("password", 255)
        val email = varchar("email", 255)
        val name = varchar("name", 255)
        val loginId = varchar("login_id", 255)
        val userStatus = long("user_status").default(UserStatus.ACTIVE.status)
        val description = varchar("description", 255).nullable()
        val isDeleted = bool("is_deleted").default(false)
    }



    class User(id: EntityID<Long>): BaseEntity(id, Users) {
        companion object : BaseEntityClass<User>(Users)
        val loginId by Users.loginId
        val name by Users.name
        val password by Users.password
        val isDeleted by Users.isDeleted
        val userStatus by Users.userStatus

        fun toAccessClaimSet(): JWTClaimsSet {
            return JWTClaimsSet.Builder()
                .claim("idx", this.id)
                .claim("id", this.loginId)
                .expirationTime(Date(Date().time + 8 * 60 * 60 * 1000 )) //8 hour
                .build()
        }
        fun toAccessClaimSetWithRoles(hasRoles: List<RoleModel.Role>): JWTClaimsSet {
            return JWTClaimsSet.Builder()
                .claim("idx", this.id)
                .claim("id", this.loginId)
                .claim("roles", hasRoles.map { it.roleCode }.toTypedArray().joinToString("|"))
                .expirationTime(Date(Date().time + 2 * 60 * 60 * 1000 )) //2 hour
                .build()
        }
    }


    data class SignUpUserDTO(
        @field:NotNull(message = "ID는 필수 값 입니다.")
        @field:NotBlank(message = "ID는 필수 값 입니다.")
        val loginId: String,
        @field:NotNull(message = "이름은 필수 값 입니다.")
        @field:NotBlank(message = "이름은 필수 값 입니다.")
        val name: String,
        @field:NotNull(message = "이메일은 필수 값 입니다.")
        @field:NotBlank(message = "이메일은 필수 값 입니다.")
        @field:Email(message = "형식에 맞는 이메일을 입력해 주세요")
        val email: String,
        @field:NotNull(message = "Password는 필수 값 입니다.")
        @field:NotBlank(message = "Password는 필수 값 입니다.")
        val password: String,
        @field:Nullable
        val description: String? = null,
    )

    data class UserUpdateDTO(
        val userIndex: Long,
        val email: String,
        val description: String?,
    )

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