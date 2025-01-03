package com.deguru.dangroa.user

import com.deguru.dangroa.dtos.User
import com.deguru.dangroa.repositories.UserRepository
import org.springframework.stereotype.Component

@Component
class UserService(
    private val userRepository: UserRepository
) {

    fun getAllUser(): List<User.UserDTO> {
        return userRepository.findAllUsers()
    }
}