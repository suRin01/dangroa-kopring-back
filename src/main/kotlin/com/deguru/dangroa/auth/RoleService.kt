package com.deguru.dangroa.auth

import com.deguru.dangroa.model.CommonRequest
import com.deguru.dangroa.model.HasRole
import com.deguru.dangroa.model.RoleModel
import com.deguru.dangroa.model.UserModel
import logger
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RoleService {
    private val log = logger()
    fun findUserRoles(userIndex: Long): List<RoleModel.Role> {
        return (HasRole.HasRoles innerJoin RoleModel.Roles)
            .selectAll()
            .where { HasRole.HasRoles.userIndex.eq(userIndex) }
            .map {
                RoleModel.Role.wrapRow(it)
            }
    }
    fun getRoleList(): List<RoleModel.Role> {
        return RoleModel.Roles.selectAll().map {
            RoleModel.Role.wrapRow(it)
        }
    }

    fun pagingRoleList(searchParam: RoleModel.RoleSearchParam, paging: CommonRequest.Paging): Pair<Long, List<RoleModel.Role>> {
        val roleModelData = RoleModel.Roles.selectAll()
        searchParam.roleName?.let {
            roleModelData.andWhere { RoleModel.Roles.roleName like "%${searchParam.roleName}%" }
        }
        val totalCount = roleModelData.count()
        roleModelData.orderBy(UserModel.Users.id, SortOrder.DESC)
            .offset((paging.pageSize * paging.pageIndex).toLong())
            .limit(paging.pageSize)


        return Pair(totalCount, roleModelData.map { RoleModel.Role.wrapRow(it) }.toList())
    }

    fun getRoleHierarchy(): List<RoleModel.RoleHierarchy> {
        val inferiorRoles = RoleModel.Roles.selectAll().alias("inferiorRole")
        return RoleModel.Roles.join(inferiorRoles,
            JoinType.LEFT,
            additionalConstraint = {RoleModel.Roles.id eq inferiorRoles[RoleModel.Roles.upperRole]})
            .selectAll()
            .map{
                RoleModel.RoleHierarchy(
                    it[RoleModel.Roles.roleName],
                    it[RoleModel.Roles.roleCode],
                    it[inferiorRoles[RoleModel.Roles.roleName]],
                    it[inferiorRoles[RoleModel.Roles.roleCode]]
                )
            }


    }

    fun addRole(newRoleModelDTO: RoleModel.NewRoleDTO): Long{
        return RoleModel.Roles.insertAndGetId {
            it[roleCode] = newRoleModelDTO.roleCode
            it[roleName] = newRoleModelDTO.roleName
            it[description] = newRoleModelDTO.description
            it[isEnabled] = newRoleModelDTO.isEnabled
            it[isDeleted] = newRoleModelDTO.isDeleted
            it[upperRole] = newRoleModelDTO.upperRole
        }.value
    }

    fun deleteRole(roleIndex: Long):Int{
        return RoleModel.Roles.update(where = {
          RoleModel.Roles.id eq roleIndex
        }, body = {
            it[isDeleted] = true
            it[isEnabled] = false
        })
    }

    fun addUserRoles(userIndex:Long, roleCodeList: ArrayList<String>) {
        val newRoleModelIndexes = RoleModel.Roles.selectAll()
            .where{
                RoleModel.Roles.roleCode inList roleCodeList
            }
            .map { it[RoleModel.Roles.id] }
        HasRole.HasRoles.batchInsert(newRoleModelIndexes) { newRoleIndex ->
            this[HasRole.HasRoles.roleIndex] = newRoleIndex
            this[HasRole.HasRoles.userIndex] = userIndex
        }
    }

    fun removeUserRoles(index: Long) {
        HasRole.HasRoles.deleteWhere {
            userIndex eq index
        }

        return;
    }

}