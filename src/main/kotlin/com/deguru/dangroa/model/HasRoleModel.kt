package com.deguru.dangroa.model

import com.deguru.dangroa.constants.UserStatus
import com.deguru.dangroa.global.BaseEntity
import com.deguru.dangroa.global.BaseLongIdTable
import com.deguru.dangroa.model.UserModel.Users
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow

class HasRoleModel {
    object HasRoles: BaseLongIdTable("has_role", "has_role_index"){
        val roleIndex = reference("role_index", RoleModel.Roles.id)
        val userIndex = reference("user_index", UserModel.Users.id)
    }

    class HasRole(id: EntityID<Long>): BaseEntity(id, HasRoles) {
        val roleIndex by HasRoles.roleIndex
        val userIndex by HasRoles.userIndex
    }

    data class HasRoleDTO(
        val hasRoleIndex: Long,
        val roleIndex: Long,
        val userIndex: Long,
    ){
        constructor(hasRoleEntity: HasRole) : this(
            hasRoleEntity.id.value,
            hasRoleEntity.roleIndex.value,
            hasRoleEntity.userIndex.value,
        )
        constructor(hasRoleRow: ResultRow) : this(
            hasRoleRow[HasRoles.id].value,
            hasRoleRow[HasRoles.roleIndex].value,
            hasRoleRow[HasRoles.userIndex].value,
        )
    }
}