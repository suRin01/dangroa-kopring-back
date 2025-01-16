package com.deguru.dangroa.user

import com.deguru.dangroa.model.CommonRequest
import com.deguru.dangroa.model.HasRole
import com.deguru.dangroa.model.UserModel
import logger
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService{

    val log = logger()
    fun getAllUser(): List<UserModel.User> {
        return UserModel.Users
            .selectAll()
            .map { UserModel.User.wrapRow(it) }
    }


    fun findUserById(id: String): UserModel.User? {
        return UserModel.Users.selectAll()
            .where(UserModel.Users.loginId.eq(id))
        .singleOrNull()?.let { UserModel.User.wrapRow(it) }
    }

    fun findUserByUserIndex(index: Long): UserModel.User? {
        return UserModel.Users.selectAll()
            .where(UserModel.Users.id.eq(index))
            .singleOrNull()?.let { UserModel.User.wrapRow(it) }
    }

    fun insertTestUser():Long{
        for (index in 10..100){
            insertUser(UserModel.SignUpUserDTO(
                "testUser_${index}",
                "testUser_${index}",
                "testUser_${index}@test.com",
                "testUser_${index}_p"
            ))
        }
        return 0
    }

    fun insertUser(userData: UserModel.SignUpUserDTO): Long{
        val id = UserModel.Users.insertAndGetId {
            it[loginId] = userData.loginId
            it[name] = userData.name
            it[email] = userData.email
            it[password] = userData.password
            it[description] = userData.description
        }
        HasRole.HasRoles.insert {
            it[userIndex] = id
            it[roleIndex] = 3
        }

        return id.value
    }


    fun searchUsers(paging: CommonRequest.Paging, searchParam: UserModel.UserSearchParam): Pair<Long, List<UserModel.User>> {
        val userData = UserModel.Users.selectAll();

        searchParam.loginId?.let {
            userData.andWhere { UserModel.Users.loginId like "%${searchParam.loginId}%" }
        }
        searchParam.email?.let {
            userData.andWhere { UserModel.Users.email like "%${searchParam.email}%" }
        }
        searchParam.name?.let {
            userData.andWhere { UserModel.Users.name like "%${searchParam.name}%" }
        }
        searchParam.userStatus?.let {
            userData.andWhere { UserModel.Users.userStatus eq searchParam.userStatus }
        }
        searchParam.isDeleted?.let {
            userData.andWhere { UserModel.Users.isDeleted eq searchParam.isDeleted }
        }

        val totalCount = userData.count()

        log.debug("totalCount = $totalCount")

        userData.orderBy(UserModel.Users.id, SortOrder.DESC)
            .offset((paging.pageSize * paging.pageIndex).toLong())
            .limit(paging.pageSize)


        return Pair(totalCount, userData.map { UserModel.User.wrapRow(it) }.toList())



    }


    fun updateUser(userData: UserModel.UserUpdateDTO): Long{
        UserModel.Users.update({ UserModel.Users.id.eq(UserModel.Users.id) }, 1){
            it[email] = userData.email
            it[description] = userData.description
        }


        return userData.userIndex
    }

    fun deleteUser(userIndex: Long): Long{
        UserModel.Users.update({ UserModel.Users.id.eq(UserModel.Users.id) }, 1){
            it[isDeleted] = true
        }

        return userIndex
    }


}