package com.deguru.dangroa.model

import jakarta.validation.constraints.NotNull
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.springframework.security.core.GrantedAuthority

class Role {
    object RolesTable: IdTable<Long>("roles") {
        override val id = long("roleIndex").uniqueIndex().autoIncrement().entityId()
        val roleName = varchar("roleName", 255)
        val roleCode = varchar("roleCode", 255)
        val description = varchar("description", 255).nullable()
        val isEnabled = bool("isEnabled")
        val isDeleted = bool("isDeleted")
        val upperRole = long("upperRole")
    }

    data class RoleDTO(
        val roleIndex: Long,
        val roleName: String,
        val roleCode: String,
        val description: String?,
        val isEnabled: Boolean,
        val isDeleted: Boolean,
        val upperRole: Long?){
        constructor(queryRow: ResultRow) : this(
            queryRow[RolesTable.id].value,
            queryRow[RolesTable.roleName],
            queryRow[RolesTable.roleCode],
            queryRow[RolesTable.description],
            queryRow[RolesTable.isEnabled],
            queryRow[RolesTable.isDeleted],
            queryRow[RolesTable.upperRole],
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