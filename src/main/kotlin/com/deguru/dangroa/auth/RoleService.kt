package com.deguru.dangroa.auth

import com.deguru.dangroa.dtos.HasRole
import com.deguru.dangroa.dtos.Role
import org.jetbrains.exposed.sql.selectAll
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

}