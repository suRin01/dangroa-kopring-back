package com.deguru.dangroa.security

import com.deguru.dangroa.user.dto.UserDTO
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import logger
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class JwtAuthenticationFilter(val jwtUtils: JwtService) : OncePerRequestFilter()  {
    val log = logger();
    fun tokenResolver (request: HttpServletRequest):String? {
        val tokenPrefix = "Bearer ";
        val token = request.getHeader("Authorization");
        if (token != null && token.startsWith(tokenPrefix)) {
            return token.substring(tokenPrefix.length);
        }

        return null;

    }
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val logger = logger();

        val token = tokenResolver(request);
        logger.debug("token: $token");
        if(Objects.isNull(token)){
            logger.debug("skip decoding!");
            filterChain.doFilter(request, response);
        }
        try {
            val claimsSet = jwtUtils.decodeJwt(token!!);
            val userKey = claimsSet.getClaim("userKey");

            //get user info
            val userData = UserDTO.User(
                userIndex = 1,
                userName = "user1 nickname",
                name = "user1 name",
                id = "user1",
                email = "",
                password = "123",
                description = null,
            )
            //set user credential
            SecurityContextHolder.getContext().setAuthentication()

        }catch (e:Exception){
            // ㅇ???
            log.debug("jwt decode error");
        }


        filterChain.doFilter(request, response);

    }

}