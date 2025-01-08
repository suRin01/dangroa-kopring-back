package com.deguru.dangroa.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component


@Component
class CustomAuthenticationEntrypoint: AuthenticationEntryPoint {
    val log = logger()
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        log.error("CustomAuthenticationEntrypoint class called")
        log.error("unauthenticated")
        log.error("unauthenticatedException: $authException")

        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.status = HttpStatus.UNAUTHORIZED.value()
        response?.characterEncoding = "UTF-8"
        response?.writer?.write("bad authentication") //.write(responseBody)
        return
    }

}