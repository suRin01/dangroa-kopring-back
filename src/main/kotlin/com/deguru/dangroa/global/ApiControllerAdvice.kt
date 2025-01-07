package com.deguru.dangroa.global

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ApiControllerAdvice {
    val log = logger()
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        log.debug("MethodArgumentNotValidException")


        val errors: MutableMap<String, String> = HashMap()
        exception.bindingResult.allErrors
            .forEach { c -> c.defaultMessage?.let { errors[(c as FieldError).field] = it } }
        return ResponseEntity.badRequest().body(errors)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException){
        when (val caused = exception.cause) {
            is MethodArgumentNotValidException -> {
                log.error("MethodArgumentNotValidException")
                val errors: MutableMap<String, String> = HashMap()
                caused.bindingResult.allErrors
                    .forEach { c -> c.defaultMessage?.let { errors[(c as FieldError).field] = it } }

            }
            is MismatchedInputException -> {
                log.error("MismatchedInputException")
                log.error("${caused.path.joinToString() { it.fieldName }} is null")
            }
            else -> throw exception
        }

        log.debug("HttpMessageNotReadableException")
    }


    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(exception: AuthenticationException): ResponseEntity<String> {
        log.debug("AuthenticationException")
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.message)
    }


    @ExceptionHandler(CommonException::class)
    fun handleCommonException(e: CommonException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            e.exceptionCode.status.value(),
            e.exceptionCode.message,
        )
        return ResponseEntity(errorResponse, e.exceptionCode.status)
    }


}