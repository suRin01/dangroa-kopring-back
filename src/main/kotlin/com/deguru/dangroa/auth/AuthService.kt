package com.deguru.dangroa.auth

import com.deguru.dangroa.model.AuthModel
import com.deguru.dangroa.model.HasRoleModel
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
    fun backdoorLogin(loginData: AuthModel.LoginDTO): AuthModel.LoginSuccessDTO {
        val lookupUser = UserModel.Users.selectAll()
            .where(UserModel.Users.loginId.eq(loginData.id))
            .firstOrNull()?.let{
                UserModel.UserDTO(it)
            }
        if (lookupUser == null) {
            log.debug("user not found or password is wrong")
            throw CommonException(ResultCode.WRONG_CREDENTIAL)
        }
        val userRoles = (HasRoleModel.HasRoles innerJoin RoleModel.Roles)
            .selectAll()
            .where { HasRoleModel.HasRoles.userIndex eq lookupUser.userIndex }
            .map {
                RoleModel.RoleDTO(it)
            }

        return AuthModel.LoginSuccessDTO(
            accessToken = jwtService.encodeJwt(jwtService.toAccessClaimSetWithRoles(lookupUser, userRoles)),
            refreshToken = jwtService.encodeJwt(jwtService.toAccessClaimSet(lookupUser)),
            id = lookupUser.loginId
        )
    }

}