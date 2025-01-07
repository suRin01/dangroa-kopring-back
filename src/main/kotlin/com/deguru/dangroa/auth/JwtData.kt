package com.deguru.dangroa.auth

import com.deguru.dangroa.dtos.User
import com.deguru.dangroa.security.JwtService
import com.deguru.dangroa.user.UserService
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import logger

class JwtData(token: String) {
    var parsedJwt: SignedJWT = JwtService().decodeJwt(token)
    var claimsSet: JWTClaimsSet = parsedJwt.jwtClaimsSet
    var tokenOwnerIndex: Long = claimsSet.claims["idx"].toString().toLong()
    var roles: List<String> = claimsSet.claims["roles"].toString().split(",")
    var userData: User.UserDTO = UserService().findUserByUserIndex(tokenOwnerIndex)!!
    var isValid: Boolean = true

}