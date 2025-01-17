package com.deguru.dangroa.security

import com.deguru.dangroa.global.CommonException
import com.deguru.dangroa.global.ResultCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import logger
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class JwtAuthenticationFilter(
    private val jwtUtils: JwtService,
    private val customUserDetailsService: CustomUserDetailService) : OncePerRequestFilter()  {
    private val log = logger()

    private fun tokenResolver (request: HttpServletRequest):String? {
        val tokenPrefix = "Bearer "
        val token = request.getHeader("Authorization")
        log.debug("Authorization header: $token")
        if (token != null && token.startsWith(tokenPrefix)) {
            return token.substring(tokenPrefix.length)
        }

        return null

    }
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val token = tokenResolver(request)
        if(Objects.isNull(token)){
            filterChain.doFilter(request, response)
            return
        }
        try {
            val decodedJwt = jwtUtils.decodeJwt(token!!);
            if(!jwtUtils.isValid(decodedJwt)){
                throw CommonException(ResultCode.JWT_ERROR)
            }

            val claimsSet = decodedJwt.jwtClaimsSet
            val userKey = claimsSet.claims["idx"].toString()

            val userDetails = customUserDetailsService.loadUserByUsername(userKey)
            log.debug("User Details: {}", userDetails)
            //set user credential
            val context: SecurityContext = SecurityContextHolder.createEmptyContext()
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            context.authentication = authentication

            SecurityContextHolder.setContext(context)
        }catch (e:Exception){
            SecurityContextHolder.getContext().authentication = null
        }


        filterChain.doFilter(request, response)

    }

}