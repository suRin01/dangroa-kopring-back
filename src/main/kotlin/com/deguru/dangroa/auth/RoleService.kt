package com.deguru.dangroa.auth

import com.deguru.dangroa.dtos.HasRole
import com.deguru.dangroa.dtos.Role
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.selectAll
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RoleService {
    fun findUserRoles(userIndex: Long): List<Role.RoleDTO> {
        return (HasRole.HasRolesTable innerJoin Role.RolesTable)
            .selectAll()
            .where { HasRole.HasRolesTable.userIndex.eq(userIndex) }
            .map {
                Role.RoleDTO(it)
            }
    }

    fun getRoleHierarchy(): List<Role.RoleHierarchy> {
        val inferiorRoles = Role.RolesTable.selectAll().alias("inferiorRole");
        return Role.RolesTable.join(inferiorRoles,
            JoinType.LEFT,
            additionalConstraint = {Role.RolesTable.roleIndex eq inferiorRoles[Role.RolesTable.upperRole]})
            .selectAll()
            .map{
                Role.RoleHierarchy(
                    it[Role.RolesTable.roleName],
                    it[Role.RolesTable.roleCode],
                    it[inferiorRoles[Role.RolesTable.roleName]],
                    it[inferiorRoles[Role.RolesTable.roleCode]]
                )
            }


    }

}