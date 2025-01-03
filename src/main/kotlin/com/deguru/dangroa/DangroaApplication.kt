package com.deguru.dangroa

import com.deguru.dangroa.dtos.HasRole
import com.deguru.dangroa.dtos.Role
import com.deguru.dangroa.dtos.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DangroaApplication

fun main(args: Array<String>) {
	runApplication<DangroaApplication>(*args)
	//test code
	Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;", driver = "org.h2.Driver")
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
			it[userName] = "user1 nickname"
			it[name] = "user1 name"
			it[email] = "12312313123123"
			it[password] = "123"
			it[description] = "12312313123123"
			it[userStatus] = 0
			it[enabled] = true
		} get User.UsersTable.userIndex

		val user2Id = User.UsersTable.insert {
			it[id] = "user2"
			it[description] = "Read the first two chapters of The Hobbit"
			it[userName] = "user2 nickname"
			it[name] = "user2 name"
			it[email] = "12312313123123"
			it[password] = "123"
			it[description] = "12312313123123"
			it[userStatus] = 0
			it[enabled] = true
		} get User.UsersTable.userIndex


		val user3Id = User.UsersTable.insert {
			it[id] = "user3"
			it[description] = "Read the first two chapters of The Hobbit"
			it[userName] = "user3 nickname"
			it[name] = "user3 name"
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
}
