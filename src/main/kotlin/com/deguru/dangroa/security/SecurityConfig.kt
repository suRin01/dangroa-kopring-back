package com.deguru.dangroa.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Order(Ordered.LOWEST_PRECEDENCE)
@Configuration
@EnableWebSecurity
class SecurityConfig(
        var jwtAuthenticationFilter: JwtAuthenticationFilter,
        var jwtAuthenticationEntrypoint: JwtAuthenticationEntrypoint,
        var customAccessDeniedHandler: CustomAccessDeniedHandler
    ) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
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
            addFilterAfter<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter)
            exceptionHandling {
                authenticationEntryPoint = jwtAuthenticationEntrypoint
                accessDeniedHandler = customAccessDeniedHandler
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