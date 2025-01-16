package com.deguru.dangroa.user

import com.deguru.dangroa.model.UserModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/user")
class UserController(val userService: UserService) {
    @GetMapping()
    fun getUser():  List<UserModel.User> {

        return userService.getAllUser()
    }

}


