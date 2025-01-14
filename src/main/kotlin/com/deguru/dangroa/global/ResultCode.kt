package com.deguru.dangroa.global

import org.springframework.http.HttpStatus

/**
 * Code table
 * prefix C  -   ok
 *
 *
 * prefix E  -   error
 *
 *
 *
 * prefix D  -   ok, but denied
 *
 *
 */
enum class ResultCode(
    val status: HttpStatus,
    val code: String,
    val message: String,) {
    OK(HttpStatus.OK, "C0000", ""),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "D0001", "중복된 이메일입니다. 다시 입력해주세요."),
    INVALID_PARAM(HttpStatus.BAD_REQUEST, "E0001", "잘못된 파라미터입니다."),
    WRONG_CREDENTIAL(HttpStatus.UNAUTHORIZED, "D0002", "잘못된 ID/PW 입니다."),
    JWT_ERROR(HttpStatus.BAD_REQUEST, "D0003", "잘못된 JWT 입니다."),
}