package com.deguru.dangroa.model

import com.deguru.dangroa.global.BaseEntity
import com.deguru.dangroa.global.BaseEntityClass
import com.deguru.dangroa.global.BaseLongIdTable
import com.deguru.dangroa.model.RoleModel.Role
import com.deguru.dangroa.model.UserModel.Users
import jakarta.validation.constraints.NotNull
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.springframework.security.core.GrantedAuthority

class RoleModel {
    object Roles: BaseLongIdTable("role", "role_index"){
        val roleName = varchar("role_name", 255)
        val roleCode = varchar("role_code", 255)
        val description = varchar("description", 255).nullable()
        val isEnabled = bool("is_enabled")
        val isDeleted = bool("is_deleted")
        val upperRole = long("upper_role")
    }

    class Role(id: EntityID<Long>): BaseEntity(id, Roles){
        companion object : BaseEntityClass<Role>(Roles)
        val roleName by Roles.roleName
        val roleCode by Roles.roleCode
        val description by Roles.description
        val isEnabled by Roles.isEnabled
        val isDeleted by Roles.isDeleted
        val upperRole by Roles.upperRole

        fun entityToDTO(role: Role): RoleDTO {
            return RoleDTO(role)
        }
    }

    data class RoleDTO(
        val roleIndex: Long,
        val roleName: String,
        val roleCode: String,
        val description: String?,
        val isEnabled: Boolean,
        val isDeleted: Boolean,
        val upperRole: Long?) {
        constructor(roleEntity: Role) : this(
            roleEntity.id.value,
            roleEntity.roleName,
            roleEntity.roleCode,
            roleEntity.description,
            roleEntity.isEnabled,
            roleEntity.isDeleted,
            roleEntity.upperRole,
        )
        constructor(roleRow: ResultRow) : this(
            roleRow[Roles.id].value,
            roleRow[Roles.roleName],
            roleRow[Roles.roleCode],
            roleRow[Roles.description],
            roleRow[Roles.isEnabled],
            roleRow[Roles.isDeleted],
            roleRow[Roles.upperRole],
        )
    }

    data class NewRoleDTO(
        val roleName: String,
        val roleCode: String,
        val description: String?,
        val isEnabled: Boolean = true,
        val isDeleted: Boolean = false,
        val upperRole: Long
    )
    data class RoleSearchParam(
        val roleName: String?
    )

    data class RoleListDTO(
        @field:NotNull
        val userIndex: Long,
        val newRoles: ArrayList<String>?,
    )

    data class RoleHierarchy(
        val roleName: String,
        val roleCode: String,
        val inferiorRoleName: String?,
        val inferiorRoleCode: String?,
    ){
        fun toAuthority(): GrantedAuthority{
            return GrantedAuthority(
                function = fun():String{
                    return "ROLE_$roleCode > ROLE_$inferiorRoleCode"
                }
            )
        }
    }
}