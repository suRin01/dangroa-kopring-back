package com.deguru.dangroa.security

import com.deguru.dangroa.repositories.RoleRepository
import com.deguru.dangroa.repositories.UserRepository
import logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Order(Ordered.LOWEST_PRECEDENCE)
@Configuration
@EnableWebSecurity
class SecurityConfig(
    var userRepository: UserRepository,
    var roleRepository: RoleRepository
    ) {
    private val log = logger()
    @Value("\${jwt.public.key}")
    private lateinit var jwtKey: String
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        log.info("Security config filter chain")
        http {
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeHttpRequests {
                authorize("/css/**", permitAll)
                authorize("/auth/login", permitAll)
                authorize("/user/**", hasAuthority("ROLE_manager"))
                authorize("/admin/**", hasAuthority("ROLE_admin"))

            }
            addFilterAfter<UsernamePasswordAuthenticationFilter>(JwtAuthenticationFilter(userRepository, roleRepository))
            exceptionHandling {
                authenticationEntryPoint = JwtAuthenticationEntrypoint()
                accessDeniedHandler = CustomAccessDeniedHandler()
            }
        }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:8081", "http://localhost:8082")
        configuration.allowedMethods = listOf("POST", "GET", "DELETE", "PUT")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }




}