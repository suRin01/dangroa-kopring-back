package com.deguru.dangroa.auth

import com.deguru.dangroa.dtos.Auth
import com.deguru.dangroa.dtos.HasRole
import com.deguru.dangroa.dtos.Role
import com.deguru.dangroa.dtos.User
import com.deguru.dangroa.global.CommonException
import com.deguru.dangroa.global.CommonExceptionCode
import com.deguru.dangroa.security.JwtService
import logger
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService(
    val jwtService: JwtService,
) {
    val log = logger()
    fun backdoorLogin(loginData: Auth.LoginDTO): Auth.LoginSuccessDTO {
        val lookupUser = User.UsersTable.selectAll()
            .where(User.UsersTable.id.eq(loginData.id))
            .firstOrNull()?.let{
                User.UserDTO(it)
            }
        if (lookupUser == null) {
            log.debug("user not found or password is wrong")
            throw CommonException(CommonExceptionCode.WRONG_CREDENTIAL)
        }
        val userRoles = (HasRole.HasRolesTable innerJoin Role.RolesTable)
            .selectAll()
            .where { HasRole.HasRolesTable.userIndex eq lookupUser.userIndex }
            .map {
                Role.RoleDTO(it)
            }

        return Auth.LoginSuccessDTO(
            accessToken = jwtService.encodeJwt(lookupUser.toAccessClaimSetWithRoles(userRoles)),
            refreshToken = jwtService.encodeJwt(lookupUser.toAccessClaimSet()),
            id = lookupUser.id
        )
    }

}