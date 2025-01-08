package com.deguru.dangroa.test

import com.deguru.dangroa.auth.RoleService
import com.deguru.dangroa.dtos.Role
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/test")
class TestController(
    val roleService: RoleService
) {

}