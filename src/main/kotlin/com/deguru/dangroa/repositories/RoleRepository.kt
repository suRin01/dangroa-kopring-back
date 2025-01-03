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
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;", driver = "org.h2.Driver")
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