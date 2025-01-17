package com.deguru.dangroa.model

import jakarta.validation.constraints.NotNull

class AuthModel {
    data class LoginDTO(
        @field:NotNull
        val id: String,
        @field:NotNull
        val password: String
    )

    data class LoginSuccessDTO(
        val id: String,
        val accessToken: String,
        val refreshToken: String
    )

}