package com.deguru.dangroa.security

import com.deguru.dangroa.global.CommonException
import com.deguru.dangroa.global.GsonSkipImplementedStrategy
import com.deguru.dangroa.global.ResultCode
import com.deguru.dangroa.model.CommonResponse
import com.nimbusds.jose.shaded.gson.Gson
import com.nimbusds.jose.shaded.gson.GsonBuilder
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import logger
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
        val e =  CommonException(ResultCode.JWT_ERROR, HashMap())

        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.status = e.exceptionCode.status.value()
        response?.characterEncoding = "UTF-8"
        response?.writer?.write(GsonBuilder()
            .setExclusionStrategies(GsonSkipImplementedStrategy()).create()
                .toJson(CommonResponse.CommonErrorResponse(e, e.detailMessages)))

    }

}