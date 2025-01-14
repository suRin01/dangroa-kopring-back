package com.deguru.dangroa.global

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val detailMessages: Map<String, String>?,
) {
    constructor(status: Int, error: String) : this(LocalDateTime.now(), status, error, emptyMap())
    constructor(status: Int, error: String, message: Map<String, String>?) : this(LocalDateTime.now(), status, error, message)
}


class CommonException(val exceptionCode: ResultCode, val detailMessages: Map<String, String>? = null): RuntimeException()


