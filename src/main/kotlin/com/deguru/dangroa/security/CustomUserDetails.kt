package com.deguru.dangroa.security

import com.deguru.dangroa.model.UserModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(userInfo: UserModel.User, authList: MutableCollection<out GrantedAuthority>) : UserDetails {
    private val id: String = userInfo.loginId
    private val password: String = userInfo.password
    private val name: String = userInfo.name
    private val userName: String = userInfo.name
    private val userStatus: Long = userInfo.userStatus
    private val enabled: Boolean = userInfo.isDeleted
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