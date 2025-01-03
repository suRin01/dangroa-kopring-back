package com.deguru.dangroa.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import logger
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component


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
        response?.sendError(HttpServletResponse.SC_FORBIDDEN)
        logger.debug("CustomAccessDeniedHandler call end");
        return
    }

}