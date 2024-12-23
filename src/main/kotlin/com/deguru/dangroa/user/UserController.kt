package com.deguru.dangroa.user

import com.deguru.dangroa.user.dto.UserDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/user")
class UserController {
    @GetMapping()
    fun getUser():  ArrayList<UserDTO.User> {
        val resultUSerList = ArrayList<UserDTO.User>()
        for (index:Int in 1..10) {
            resultUSerList.add(UserDTO.User(
                userIndex = 1,
                userName = "user${index} nickname",
                name = "user${index}",
                id = "user${index} name",
                email = "",
                password = "123",
                description = null
            ))
        }

        return resultUSerList;
    }
}


