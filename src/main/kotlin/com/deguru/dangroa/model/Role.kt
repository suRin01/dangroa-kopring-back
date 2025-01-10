package com.deguru.dangroa.model

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.springframework.security.core.GrantedAuthority

class Role {
    object RolesTable: IdTable<Long>("roles") {
        override val id = long("roleIndex").uniqueIndex().autoIncrement().entityId()
        val roleName = varchar("roleName", 255)
        val roleCode = varchar("roleCode", 255)
        val description = varchar("description", 255)
        val isEnabled = bool("isEnabled")
        val isDeleted = bool("isDeleted")
        val upperRole = long("upperRole")
    }

    data class RoleDTO(
        val roleIndex: Long,
        val roleName: String,
        val roleCode: String,
        val description: String,
        val isEnabled: Boolean,
        val isDeleted: Boolean,
        val upperRole: Long){
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