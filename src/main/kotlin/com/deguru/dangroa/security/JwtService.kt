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
import org.springframework.stereotype.Component


@Component
class JwtService {
    @Value("\${jwt.key}")
    private lateinit var jwtKeyString: String
    private lateinit var sharedSecret: ByteArray
    private lateinit var signer: JWSSigner
    private lateinit var verifier: JWSVerifier
    private val log = logger()

    private fun getSharedSecret(): ByteArray{
        if(!this::sharedSecret.isInitialized){
            this.sharedSecret = jwtKeyString.toByteArray()
        }
        return this.sharedSecret
    }

    private fun getSigner(): JWSSigner{
        if(!this::signer.isInitialized){
            this.signer = MACSigner(getSharedSecret())
        }
        return this.signer
    }
    private fun getVerifier(): JWSVerifier{
        if(!this::verifier.isInitialized){
            this.verifier = MACVerifier(getSharedSecret())
        }
        return this.verifier
    }
    fun encodeJwt(claimsSet: JWTClaimsSet): String {
        val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claimsSet)
        signedJWT.sign(getSigner())

        return signedJWT.serialize()
    }

    fun isValid(jwt: SignedJWT): Boolean {
        log.debug("jwtKeyString: $jwtKeyString")
        return jwt.verify(getVerifier())
    }


    fun decodeJwt(jwtString: String): SignedJWT {
        return SignedJWT.parse(jwtString)
    }

}