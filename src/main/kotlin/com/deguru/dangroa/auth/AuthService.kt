package com.deguru.dangroa.auth

import com.deguru.dangroa.model.Auth
import com.deguru.dangroa.model.HasRole
import com.deguru.dangroa.model.RoleModel
import com.deguru.dangroa.model.UserModel
import com.deguru.dangroa.global.CommonException
import com.deguru.dangroa.global.ResultCode
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
        val lookupUser = UserModel.Users.selectAll()
            .where(UserModel.Users.loginId.eq(loginData.id))
            .firstOrNull()?.let{
                UserModel.User.wrapRow(it)
            }
        if (lookupUser == null) {
            log.debug("user not found or password is wrong")
            throw CommonException(ResultCode.WRONG_CREDENTIAL)
        }
        val userRoles = (HasRole.HasRoles innerJoin RoleModel.Roles)
            .selectAll()
            .where { HasRole.HasRoles.userIndex eq lookupUser.id }
            .map {
                RoleModel.Role.wrapRow(it)
            }

        return Auth.LoginSuccessDTO(
            accessToken = jwtService.encodeJwt(lookupUser.toAccessClaimSetWithRoles(userRoles)),
            refreshToken = jwtService.encodeJwt(lookupUser.toAccessClaimSet()),
            id = lookupUser.loginId
        )
    }

}