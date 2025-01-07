package com.deguru.dangroa.user

import com.deguru.dangroa.dtos.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/user")
class UserController(val userService: UserService) {
    @GetMapping()
    fun getUser():  List<User.UserDTO> {

        return userService.getAllUser()
    }

}


