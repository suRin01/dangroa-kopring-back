package com.deguru.dangroa

import com.deguru.dangroa.dtos.UserDTO
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
		SchemaUtils.create(UserDTO.Users)

		val taskId = UserDTO.Users.insert {
			it[id] = "user1"
			it[userName] = "user1 nickname"
			it[name] = "user1 name"
			it[email] = "12312313123123"
			it[password] = "123"
			it[description] = "12312313123123"
			it[userStatus] = 0
			it[enabled] = true
		} get UserDTO.Users.id

		val secondTaskId = UserDTO.Users.insert {
			it[id] = "user2"
			it[description] = "Read the first two chapters of The Hobbit"
			it[userName] = "user2 nickname"
			it[name] = "user2 name"
			it[email] = "12312313123123"
			it[password] = "123"
			it[description] = "12312313123123"
			it[userStatus] = 0
			it[enabled] = true
		} get UserDTO.Users.id

		println("Created new tasks with ids $taskId and $secondTaskId.")

		UserDTO.Users.select(UserDTO.Users.id.count(), UserDTO.Users.enabled).groupBy(UserDTO.Users.enabled).forEach {
			println("${it[UserDTO.Users.enabled]}: ${it[UserDTO.Users.id.count()]} ")
		}
	}
}
