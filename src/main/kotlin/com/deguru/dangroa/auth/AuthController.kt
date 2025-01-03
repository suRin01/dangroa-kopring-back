package com.deguru.dangroa.auth

import logger
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/auth")
class AuthController {
    val log = logger()

    @GetMapping("/login")
    fun login(): String {

        log.debug(SecurityContextHolder.getContext().authentication.isAuthenticated.toString())
        log.debug(SecurityContextHolder.getContext().authentication.authorities.toString())
        log.debug(SecurityContextHolder.getContext().authentication.principal.toString())
        return "login test"
    }
}