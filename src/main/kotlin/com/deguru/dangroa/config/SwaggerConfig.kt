package com.deguru.dangroa.config

import com.deguru.dangroa.security.JwtService
import com.deguru.dangroa.user.UserService
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {

    private fun apiInfo(): Info {
        return Info()
            .title("deguru backend") // API의 제목
            .version("1.0.0") // API의 버전
    }
    @Bean
    fun openAPI(jwtService: JwtService, userService: UserService): OpenAPI {

        val jwt = "JWT"
        val securityRequirement: SecurityRequirement = SecurityRequirement().addList(jwt)

        val components = Components().addSecuritySchemes(
            jwt, SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description(
                        """
                            test admin jwt ::: ${jwtService.encodeJwt(userService.findUserByUserIndex(1)!!.toAccessClaimSet())}<br/>
                            test manager jwt ::: ${jwtService.encodeJwt(userService.findUserByUserIndex(2)!!.toAccessClaimSet())}<br/>
                            test user jwt ::: ${jwtService.encodeJwt(userService.findUserByUserIndex(3)!!.toAccessClaimSet())}<br/>
                        """.trimIndent())
        )


        return OpenAPI()
            .components(Components())
            .info(apiInfo())
            .addSecurityItem(securityRequirement)
            .components(components)
    }

    @Bean
    fun all(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("전체")
            .pathsToMatch("/**")
            .build()
    }

    @Bean
    fun getHomepageGroup(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("Admin")
            .pathsToMatch("/admin/**")
            .build()
    }
    @Bean
    fun getAuthGroup(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("Auth")
            .pathsToMatch("/auth/**")
            .build()
    }

    @Bean
    fun getUserGroup(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("User")
            .pathsToMatch("/user/**")
            .build()
    }
}