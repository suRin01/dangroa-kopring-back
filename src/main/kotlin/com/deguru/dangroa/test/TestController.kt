package com.deguru.dangroa.test

import com.deguru.dangroa.auth.RoleService
import com.deguru.dangroa.user.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController(
    val roleService: RoleService,
    val userService: UserService
) {

    @GetMapping("/userRoleOnly")
    fun userRoleOnly():String {
        //userService.insertTestUser()
        return "may be user role holder"
    }
}