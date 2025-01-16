package com.deguru.dangroa.auth

import com.deguru.dangroa.model.Auth
import com.deguru.dangroa.model.UserModel
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService,
) {
    val log = logger()
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody signupDTO: UserModel.SignUpUserDTO, bindingResult: BindingResult):ResponseEntity<Number> {
        if(bindingResult.hasErrors()){
            log.error("Error during signup: {}", bindingResult.fieldErrors)
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        log.debug("signup data: {}", signupDTO.toString())

        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid user: Auth.LoginDTO): Auth.LoginSuccessDTO {

        return authService.backdoorLogin(user)
    }


    @GetMapping
    fun logout(request: HttpServletRequest, response: HttpServletResponse) {
        val nullAccessToken = Cookie("accessToken", null)
        nullAccessToken.maxAge = 0
        val nullRefreshToken = Cookie("refreshToken", null)
        nullRefreshToken.maxAge = 0
        response.addCookie(nullAccessToken)
        response.addCookie(nullRefreshToken)

    }

}