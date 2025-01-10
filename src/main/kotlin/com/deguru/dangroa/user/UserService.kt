package com.deguru.dangroa.user

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
class UserService{

    val log = logger()
    fun getAllUser(): List<User.UserDTO> {
        return User.UsersTable
            .selectAll()
            .map { User.UserDTO(it) }
    }


    fun findUserById(id: String): User.UserDTO? {
        return User.UsersTable.selectAll()
            .where(User.UsersTable.loginId.eq(id))
        .singleOrNull()?.let { User.UserDTO(it) }
    }

    fun findUserByUserIndex(index: Long): User.UserDTO? {
        return User.UsersTable.selectAll()
            .where(User.UsersTable.id.eq(index))
            .singleOrNull()?.let { User.UserDTO(it) }
    }

    fun insertTestUser():Long{
        for (index in 10..100){
            insertUser(User.SignUpUserDTO(
                "testUser_${index}",
                "testUser_${index}",
                "testUser_${index}@test.com",
                "testUser_${index}_p"
            ))
        }
        return 0
    }

    fun insertUser(userData: User.SignUpUserDTO): Long{
        val id = User.UsersTable.insertAndGetId {
            it[loginId] = userData.loginId
            it[name] = userData.name
            it[email] = userData.email
            it[password] = userData.password
            it[description] = userData.description
        }
        HasRole.HasRolesTable.insert {
            it[userIndex] = id
            it[roleIndex] = 3
        }

        return id.value
    }


    fun searchUsers(paging: CommonRequest.Paging, searchParam: User.UserSearchParam): Pair<Long, List<User.UserDTO>> {
        val userData = User.UsersTable.selectAll();

        searchParam.loginId?.let {
            userData.andWhere { User.UsersTable.loginId like "%${searchParam.loginId}%" }
        }
        searchParam.email?.let {
            userData.andWhere { User.UsersTable.email like "%${searchParam.email}%" }
        }
        searchParam.name?.let {
            userData.andWhere { User.UsersTable.name like "%${searchParam.name}%" }
        }
        searchParam.userStatus?.let {
            userData.andWhere { User.UsersTable.userStatus eq searchParam.userStatus }
        }
        searchParam.isDeleted?.let {
            userData.andWhere { User.UsersTable.isDeleted eq searchParam.isDeleted }
        }

        val totalCount = userData.count()

        log.debug("totalCount = $totalCount")

        userData.orderBy(User.UsersTable.id, SortOrder.DESC)
            .offset((paging.pageSize * paging.pageIndex).toLong())
            .limit(paging.pageSize)


        return Pair(totalCount, userData.map { User.UserDTO(it) }.toList())



    }


    fun updateUser(userData: User.UserUpdateDTO): Long{
        User.UsersTable.update({ User.UsersTable.id.eq(User.UsersTable.id) }, 1){
            it[email] = userData.email
            it[description] = userData.description
        }


        return userData.userIndex
    }


}