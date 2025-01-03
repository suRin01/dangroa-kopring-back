package com.deguru.dangroa.security

import com.deguru.dangroa.repositories.RoleRepository
import com.deguru.dangroa.repositories.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import logger
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class JwtAuthenticationFilter(
    val jwtUtils: JwtService,
    val userRepository: UserRepository,
    val roleRepository: RoleRepository) : OncePerRequestFilter()  {
    val log = logger()
    fun tokenResolver (request: HttpServletRequest):String? {
        val tokenPrefix = "Bearer "
        val token = request.getHeader("Authorization")
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
        val logger = logger()

        val token = tokenResolver(request)
        logger.debug("token: ${token.toString()}")
        if(Objects.isNull(token)){
            logger.debug("skip decoding!")
            filterChain.doFilter(request, response)
        }
        try {
            val claimsSet = jwtUtils.decodeJwt(token!!)
            val userKey = claimsSet.claims.get("id").toString()

            //get user info

            val userData = userRepository.findSingleUserById(userKey)
            val authorities = ArrayList<GrantedAuthority>()
            roleRepository.findUserRoles(userData.userIndex)
                .forEach() {
                    authorities.add(SimpleGrantedAuthority("ROLE_${it.roleName}"))
                }
            //set user credential
            val authentication = UsernamePasswordAuthenticationToken(userData, null, authorities)
            SecurityContextHolder.getContext().authentication = authentication
            filterChain.doFilter(request, response)

            return
        }catch (e:Exception){
            SecurityContextHolder.getContext().authentication = null
            log.debug("jwt decode error")
        }


        filterChain.doFilter(request, response)

    }

}