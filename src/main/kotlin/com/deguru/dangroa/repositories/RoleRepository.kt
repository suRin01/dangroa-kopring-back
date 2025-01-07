package com.deguru.dangroa.repositories

import com.deguru.dangroa.dtos.HasRole
import com.deguru.dangroa.dtos.Role
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class RoleRepository {

    fun findUserRoles(userIndex: Long): List<Role.RoleDTO> {
        val queryResult = transaction {
            (HasRole.HasRolesTable innerJoin Role.RolesTable)
                .selectAll()
                .where { HasRole.HasRolesTable.userIndex eq userIndex }
                .map {
                    Role.RoleDTO(it)

                }
        }

        return queryResult
    }
}