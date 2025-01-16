package com.deguru.dangroa.auth

import com.deguru.dangroa.model.CommonRequest
import com.deguru.dangroa.model.HasRole
import com.deguru.dangroa.model.Role
import com.deguru.dangroa.model.User
import logger
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RoleService {
    private val log = logger()
    fun findUserRoles(userIndex: Long): List<Role.RoleDTO> {
        return (HasRole.HasRolesTable innerJoin Role.RolesTable)
            .selectAll()
            .where { HasRole.HasRolesTable.userIndex.eq(userIndex) }
            .map {
                Role.RoleDTO(it)
            }
    }
    fun getRoleList(): List<Role.RoleDTO> {
        return Role.RolesTable.selectAll().map {
            Role.RoleDTO(it)
        }
    }

    fun pagingRoleList(searchParam: Role.RoleSearchParam, paging: CommonRequest.Paging): Pair<Long, List<Role.RoleDTO>> {
        val roleData = Role.RolesTable.selectAll()
        searchParam.roleName?.let {
            roleData.andWhere { Role.RolesTable.roleName like "%${searchParam.roleName}%" }
        }
        val totalCount = roleData.count()
        roleData.orderBy(User.UsersTable.id, SortOrder.DESC)
            .offset((paging.pageSize * paging.pageIndex).toLong())
            .limit(paging.pageSize)


        return Pair(totalCount, roleData.map { Role.RoleDTO(it) }.toList())
    }

    fun getRoleHierarchy(): List<Role.RoleHierarchy> {
        val inferiorRoles = Role.RolesTable.selectAll().alias("inferiorRole")
        return Role.RolesTable.join(inferiorRoles,
            JoinType.LEFT,
            additionalConstraint = {Role.RolesTable.id eq inferiorRoles[Role.RolesTable.upperRole]})
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

    fun addRole(newRoleDTO: Role.NewRoleDTO): Long{
        return Role.RolesTable.insertAndGetId {
            it[roleCode] = newRoleDTO.roleCode
            it[roleName] = newRoleDTO.roleName
            it[description] = newRoleDTO.description
            it[isEnabled] = newRoleDTO.isEnabled
            it[isDeleted] = newRoleDTO.isDeleted
            it[upperRole] = newRoleDTO.upperRole
        }.value
    }

    fun deleteRole(roleIndex: Long):Int{
        return Role.RolesTable.update(where = {
          Role.RolesTable.id eq roleIndex
        }, body = {
            it[isDeleted] = true
            it[isEnabled] = false
        })
    }

    fun addUserRoles(userIndex:Long, roleCodeList: ArrayList<String>) {
        val newRoleIndexes = Role.RolesTable.selectAll()
            .where{
                Role.RolesTable.roleCode inList roleCodeList
            }
            .map { it[Role.RolesTable.id] }
        HasRole.HasRolesTable.batchInsert(newRoleIndexes) { newRoleIndex ->
            this[HasRole.HasRolesTable.roleIndex] = newRoleIndex
            this[HasRole.HasRolesTable.userIndex] = userIndex
        }
    }

    fun removeUserRoles(index: Long) {
        HasRole.HasRolesTable.deleteWhere {
            userIndex eq index
        }

        return;
    }

}