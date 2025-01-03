package com.deguru.dangroa.security

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtService {
    val logger = logger()

    @Value("\${jwt.public.key}")
    private lateinit var key: String

    private var sharedSecret: ByteArray? = null
    private var signer: JWSSigner? = null

    private fun getSharedSecret(): ByteArray{
        if(Objects.isNull(this.sharedSecret)){
            this.sharedSecret = key!!.toByteArray()
        }
        return this.sharedSecret as ByteArray
    }

    private fun getSigner(): JWSSigner{
        if(Objects.isNull(this.signer)){
            val sharedSecret = key!!.toByteArray()
            this.signer = MACSigner(sharedSecret)
        }
        return this.signer as JWSSigner
    }

    private fun testClaimsSet(): JWTClaimsSet? {
        return JWTClaimsSet.Builder()
            .claim("testId", "testValue")
            .claim("jwkId", 1)
            .issuer("test!")
            .audience("testUserId")
            .expirationTime(Date(Date().time + 2 * 60 * 60 * 1000 )) //2 hour
            .build()

    }

    fun encodeJwt(claimsSet: JWTClaimsSet ): String{
        val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claimsSet)
        signedJWT.sign(getSigner())
        val s = signedJWT.serialize()

        return s
    }


    fun decodeJwt(jwtString: String): JWTClaimsSet {
        val signedJWT = SignedJWT.parse(jwtString)
        val verifier: JWSVerifier = MACVerifier(getSharedSecret())
        val isVerified = signedJWT.verify(verifier)

        logger.debug("isVerified = $isVerified")
        logger.debug("header = {}", signedJWT.header)


        return signedJWT.getJWTClaimsSet()
    }

    fun getAuthentication(token: String): Authentication? {
        // 토큰을 이용해서 Claims 만듬

        val claims: JWTClaimsSet = decodeJwt(token)

        // Claims 에 들어있는 권한들을 가져옴
        val rolesString = claims.getClaim("roles") as String
        if(Objects.isNull(rolesString)){
            return null
        }


        val authorities: Collection<GrantedAuthority?> = rolesString.split("|").map { SimpleGrantedAuthority(it) }


        // 권한 정보를 이용해서 User 객체를 만듬
        val principal: User = User("username", "", authorities)


        //mutableListOf<GrantedAuthority>(SimpleGrantedAuthority("ROLE_USER"))

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

}