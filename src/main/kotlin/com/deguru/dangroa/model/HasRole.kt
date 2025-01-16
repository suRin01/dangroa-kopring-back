package com.deguru.dangroa.model

import com.deguru.dangroa.global.BaseEntity
import com.deguru.dangroa.global.BaseLongIdTable
import org.jetbrains.exposed.dao.id.EntityID

class HasRole {
    object HasRoles: BaseLongIdTable("has_role", "has_role_index"){
        val roleIndex = reference("role_index", RoleModel.Roles.id)
        val userIndex = reference("user_index", UserModel.Users.id)
    }

    class HasRole(id: EntityID<Long>): BaseEntity(id, HasRoles) {
        val roleIndex by HasRoles.roleIndex
        val userIndex by HasRoles.userIndex
    }
}