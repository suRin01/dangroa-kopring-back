package com.deguru.dangroa.security

import com.deguru.dangroa.auth.RoleService
import com.deguru.dangroa.user.UserService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CustomUserDetailService(
    val userService: UserService,
    val roleService: RoleService): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val userData = userService.findUserById(username!!)
        val roleData = userData?.let { it ->
            roleService
                .findUserRoles(it.userIndex)
                .map{
                    GrantedAuthority(function = fun():String{
                        return "ROLE_${it.roleCode}"
                    })
                }.toMutableSet()
        }

        return CustomUserDetails(userData!!, roleData!!)


    }

}