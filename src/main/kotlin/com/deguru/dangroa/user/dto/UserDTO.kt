package com.deguru.dangroa.user.dto

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

class UserDTO {

    @Entity
    data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val userIndex: Long,
        val userName:String,
        val password:String,
        val email:String,
        val name:String,
        val id:String,
        val description: String?
    )
}