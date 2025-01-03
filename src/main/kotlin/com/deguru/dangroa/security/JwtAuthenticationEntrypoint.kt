package com.deguru.dangroa.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import logger
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component


class JwtAuthenticationEntrypoint: AuthenticationEntryPoint {
    val logger = logger()
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        logger.error("JwtAuthenticationEntrypoint class called")
        logger.error("unauthenticated")
        logger.error("unauthenticatedException: $authException")
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED)
        return
    }

}