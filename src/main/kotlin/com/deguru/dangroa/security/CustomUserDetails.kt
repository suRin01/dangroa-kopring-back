package com.deguru.dangroa.security

import com.deguru.dangroa.dtos.User
import org.jetbrains.exposed.sql.Column
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(userInfo: User.UsersTable, authList: MutableCollection<out GrantedAuthority>) : UserDetails {
    private val id: String = userInfo.id.toString()
    private val password: String = userInfo.password.toString()
    private val name: String = userInfo.name.toString()
    private val userName: String = userInfo.name.toString()
    private val userStatus: Column<Long> = userInfo.userStatus
    private val enabled: Column<Boolean> = userInfo.enabled
    private val userInfoAuthList: MutableCollection<out GrantedAuthority> = authList

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.userInfoAuthList
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return userName
    }
}