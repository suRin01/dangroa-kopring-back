package com.deguru.dangroa.global

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: Map<String, String>?,
) {
    constructor(status: Int, error: String) : this(LocalDateTime.now(), status, error, emptyMap())
    constructor(status: Int, error: String, message: Map<String, String>?) : this(LocalDateTime.now(), status, error, message)
}
enum class CommonExceptionCode(
    val status: HttpStatus,
    val message: String,) {
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다. 다시 입력해주세요."),
    WRONG_CREDENTIAL(HttpStatus.UNAUTHORIZED, "잘못된 ID/PW 입니다.")
}

class CommonException(val exceptionCode: CommonExceptionCode): RuntimeException()


