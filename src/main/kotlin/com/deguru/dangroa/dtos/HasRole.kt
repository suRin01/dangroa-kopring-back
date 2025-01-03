package com.deguru.dangroa.dtos

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

class HasRole {
    object HasRolesTable: Table("hasRoles") {
        val hasRoleIndex = long("hasRoleIndex").uniqueIndex().autoIncrement()
        val roleIndex = long("roleIndex") references Role.RolesTable.roleIndex
        val userIndex = long("userIndex") references User.UsersTable.userIndex
    }

    data class HasRoleDTO(
        val hasRoleIndex: Long,
        val roleIndex: Long,
        val userIndex: Long){
        constructor(queryRow: ResultRow) : this(
            queryRow[HasRolesTable.hasRoleIndex],
            queryRow[HasRolesTable.roleIndex],
            queryRow[HasRolesTable.userIndex]
        )

    }
}