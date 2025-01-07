package com.deguru.dangroa.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component


@Component
class CustomAccessDeniedHandler: AccessDeniedHandler {
    val logger = logger()
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        logger.error("CustomAccessDeniedHandler class called")
        logger.error("unauthorized")
        logger.error("unauthorizedException: $accessDeniedException")


        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.status = HttpStatus.FORBIDDEN.value()
        response?.characterEncoding = "UTF-8"
        response?.writer?.write("bad authorization") //.write(responseBody)

        return
    }

}