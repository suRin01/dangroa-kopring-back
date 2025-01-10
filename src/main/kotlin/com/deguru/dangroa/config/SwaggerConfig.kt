package com.deguru.dangroa.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val jwt = "JWT"
        val securityRequirement: SecurityRequirement = SecurityRequirement().addList(jwt)

        val components = Components().addSecuritySchemes(
            jwt, SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        )
        return OpenAPI()
            .components(Components())
            .info(apiInfo())
            .addSecurityItem(securityRequirement)
            .components(components)
    }


    private fun apiInfo(): Info {
        return Info()
            .title("API Test") // API의 제목
            .description("Let's practice Swagger UI") // API에 대한 설명
            .version("1.0.0") // API의 버전
    }
}