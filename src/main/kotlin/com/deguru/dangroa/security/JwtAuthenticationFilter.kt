package com.deguru.dangroa.security

import com.deguru.dangroa.auth.JwtData
import com.deguru.dangroa.auth.RoleService
import com.deguru.dangroa.user.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import logger
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class JwtAuthenticationFilter(
    private val roleService: RoleService,
    private val userService: UserService,
    private val jwtUtils: JwtService,) : OncePerRequestFilter()  {
    private val log = logger()


    private fun tokenResolver (request: HttpServletRequest):String? {
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

        val token = tokenResolver(request)
        log.debug("token: ${token.toString()}")
        if(Objects.isNull(token)){
            log.debug("skip decoding!")
            filterChain.doFilter(request, response)
            return
        }
        try {
            val jwtData = JwtData(token!!)
            log.debug("token classfying: ${jwtData.toString()}")
            val decodedJwt = jwtUtils.decodeJwt(token!!);


            val claimsSet = jwtUtils.decodeJwt(token!!).jwtClaimsSet
            val userKey = claimsSet.claims.get("id").toString()

            //get user info

            val userData = userService.findUserById(userKey)
            if(userData == null) {
                filterChain.doFilter(request, response)
            }
            val authorities = ArrayList<GrantedAuthority>()
            roleService.findUserRoles(userData!!.userIndex).forEach() {
                authorities.add(SimpleGrantedAuthority("ROLE_${it.roleName}"))
            }



            //set user credential
            val context: SecurityContext = SecurityContextHolder.createEmptyContext()
            val authentication = UsernamePasswordAuthenticationToken(userData, null, authorities)
            context.authentication = authentication

            SecurityContextHolder.setContext(context)
            log.debug("is authenticated: ${SecurityContextHolder.getContext().authentication.isAuthenticated}")
            log.debug("authentication success")
        }catch (e:Exception){
            SecurityContextHolder.getContext().authentication = null
            e.printStackTrace()
            log.debug("jwt decode error")
        }


        filterChain.doFilter(request, response)

    }

}