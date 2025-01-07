package com.deguru.dangroa.user

import com.deguru.dangroa.dtos.HasRole
import com.deguru.dangroa.dtos.Role
import com.deguru.dangroa.dtos.User
import com.deguru.dangroa.repositories.UserRepository
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class UserService(
    private val userRepository: UserRepository
) {

    fun getAllUser(): List<User.UserDTO> {
        return userRepository.findAllUsers()
    }


    fun insertTestUser(): Long {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.drop(User.UsersTable, Role.RolesTable, HasRole.HasRolesTable)
            SchemaUtils.create(User.UsersTable, Role.RolesTable, HasRole.HasRolesTable)

            val adminRoleIdx = Role.RolesTable.insert {
                it[roleName] = "Admin"
                it[roleCode] = "A"
                it[isEnabled] = true
                it[isDeleted] = false
                it[description] = "test role 1"

            } get Role.RolesTable.roleIndex

            val managerRoleIdx = Role.RolesTable.insert {
                it[roleName] = "manager"
                it[roleCode] = "M"
                it[isEnabled] = true
                it[isDeleted] = false
                it[description] = "test role 2"
            } get Role.RolesTable.roleIndex


            val userRoleIdx = Role.RolesTable.insert {
                it[roleName] = "user"
                it[roleCode] = "U"
                it[isEnabled] = true
                it[isDeleted] = false
                it[description] = "test role 3"
            } get Role.RolesTable.roleIndex

            val user1Id = User.UsersTable.insert {
                it[id] = "user1"
                it[this.name] = "user1 nickname"
                it[this.name] = "user1 name"
                it[email] = "12312313123123"
                it[password] = "123"
                it[description] = "12312313123123"
                it[userStatus] = 0
                it[enabled] = true
            } get User.UsersTable.userIndex

            val user2Id = User.UsersTable.insert {
                it[id] = "user2"
                it[description] = "Read the first two chapters of The Hobbit"
                it[this.name] = "user2 nickname"
                it[this.name] = "user2 name"
                it[email] = "12312313123123"
                it[password] = "123"
                it[description] = "12312313123123"
                it[userStatus] = 0
                it[enabled] = true
            } get User.UsersTable.userIndex


            val user3Id = User.UsersTable.insert {
                it[id] = "user3"
                it[description] = "Read the first two chapters of The Hobbit"
                it[this.name] = "user3 nickname"
                it[this.name] = "user3 name"
                it[email] = "12312313123123"
                it[password] = "123"
                it[description] = "12312313123123"
                it[userStatus] = 0
                it[enabled] = true
            } get User.UsersTable.userIndex

            //relation between user and role
            HasRole.HasRolesTable.insert {
                it[roleIndex] = adminRoleIdx
                it[userIndex] = user1Id
            }

            HasRole.HasRolesTable.insert {
                it[roleIndex] = managerRoleIdx
                it[userIndex] = user1Id
            }

            HasRole.HasRolesTable.insert {
                it[roleIndex] = userRoleIdx
                it[userIndex] = user1Id
            }

            HasRole.HasRolesTable.insert {
                it[roleIndex] = managerRoleIdx
                it[userIndex] = user2Id
            }

            HasRole.HasRolesTable.insert {
                it[roleIndex] = userRoleIdx
                it[userIndex] = user2Id
            }

            HasRole.HasRolesTable.insert {
                it[roleIndex] = userRoleIdx
                it[userIndex] = user3Id
            }


        }

        return 1
    }
}