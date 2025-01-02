package com.deguru.dangroa.security

import com.deguru.dangroa.dtos.UserDTO
import com.deguru.dangroa.repositories.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import logger
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class JwtAuthenticationFilter(val jwtUtils: JwtService, val userRepository: UserRepository) : OncePerRequestFilter()  {
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
        logger.debug("token: ${token.toString()}");
        if(Objects.isNull(token)){
            logger.debug("skip decoding!");
            filterChain.doFilter(request, response);
        }
        try {
            val claimsSet = jwtUtils.decodeJwt(token!!);
            val userKey = claimsSet.getClaim("userKey");

            //get user info

            val userData = userRepository.findUserById(claimsSet.claims.get("id").toString());
            log.debug("userData: {}", userData);
            //set user credential
            //SecurityContextHolder.getContext().setAuthentication()

        }catch (e:Exception){
            // ㅇ???
            e.printStackTrace();
            log.debug("jwt decode error");
        }


        filterChain.doFilter(request, response);

    }

}