package com.deguru.dangroa.model

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow

class HasRole {
    object HasRolesTable: IdTable<Long>("hasRoles") {
        override val id = long("hasRoleIndex").uniqueIndex().autoIncrement().entityId()
        val roleIndex = reference("roleIndex", Role.RolesTable.id)
        val userIndex = reference("userIndex", User.UsersTable.id)
    }

    data class HasRoleDTO(
        val hasRoleIndex: Long,
        val roleIndex: Long,
        val userIndex: Long){
        constructor(queryRow: ResultRow) : this(
            queryRow[HasRolesTable.id].value,
            queryRow[HasRolesTable.roleIndex].value,
            queryRow[HasRolesTable.userIndex].value
        )

    }
}