package com.deguru.dangroa.dtos

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

class Role {
    object RolesTable: Table("roles") {
        val roleIndex = long("roleIndex").uniqueIndex().autoIncrement()
        val roleName = varchar("roleName", 255)
        val roleCode = varchar("roleCode", 255)
        val description = varchar("description", 255)
        val isEnabled = bool("isEnabled")
        val isDeleted = bool("isDeleted")
    }

    data class RoleDTO(
        val roleIndex: Long,
        val roleName: String,
        val roleCode: String,
        val description: String,
        val isEnabled: Boolean,
        val isDeleted: Boolean){
        constructor(queryRow: ResultRow) : this(
            queryRow[RolesTable.roleIndex],
            queryRow[RolesTable.roleName],
            queryRow[RolesTable.roleCode],
            queryRow[RolesTable.description],
            queryRow[RolesTable.isEnabled],
            queryRow[RolesTable.isDeleted]
        )

    }
}