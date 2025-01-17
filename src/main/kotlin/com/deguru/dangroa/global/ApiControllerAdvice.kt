package com.deguru.dangroa.global

import com.deguru.dangroa.model.CommonResponse
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import jakarta.validation.ConstraintViolationException
import logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException


@RestControllerAdvice
class ApiControllerAdvice {
    val log = logger()
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<CommonResponse.CommonErrorResponse> {

        val errors: MutableMap<String, String> = HashMap()
        exception.bindingResult.allErrors
            .forEach { c -> c.defaultMessage?.let { errors[(c as FieldError).field] = it } }

        val e =  CommonException(ResultCode.INVALID_PARAM, errors)
        return ResponseEntity.status(e.exceptionCode.status).body(CommonResponse.CommonErrorResponse(e, e.detailMessages))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException):ResponseEntity<CommonResponse.CommonErrorResponse>{
        val errors: MutableMap<String, String> = HashMap()
        when (val caused = exception.cause) {
            is MethodArgumentNotValidException -> {
                caused.bindingResult.allErrors
                    .forEach { c -> c.defaultMessage?.let { errors[(c as FieldError).field] = it } }
            }
            is MismatchedInputException -> {
                caused.path.forEach{
                    errors[it.fieldName] = "${it.fieldName}은 null 일 수 없습니다."
                }
            }
            else -> throw exception
        }

        val e =  CommonException(ResultCode.INVALID_PARAM, errors)
        return ResponseEntity.status(e.exceptionCode.status).body(CommonResponse.CommonErrorResponse(e, e.detailMessages))
    }


    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(exception: AuthenticationException): ResponseEntity<String> {
        log.debug("AuthenticationException")
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.message)
    }


    @ExceptionHandler(CommonException::class)
    fun handleCommonException(e: CommonException): ResponseEntity<CommonResponse.CommonErrorResponse> {

        return ResponseEntity.status(e.exceptionCode.status).body(CommonResponse.CommonErrorResponse(e, e.detailMessages))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidationException(exception: ConstraintViolationException): ResponseEntity<CommonResponse.CommonErrorResponse> {
        val errors: MutableMap<String, String> = HashMap()
        exception.constraintViolations.forEach {
            errors[it.constraintDescriptor.validationAppliesTo.name] = it.message
        }

        val e =  CommonException(ResultCode.INVALID_PARAM, errors)
        return ResponseEntity.status(e.exceptionCode.status).body(CommonResponse.CommonErrorResponse(e, e.detailMessages))
    }


    @ExceptionHandler(NoResourceFoundException::class)
    fun handleResourceNotFoundException(exception: NoResourceFoundException): ResponseEntity<CommonResponse.CommonErrorResponse> {
        val errors: MutableMap<String, String> = HashMap()
        val e =  CommonException(ResultCode.RESOURCE_NOT_FOUND, errors)
        return ResponseEntity.status(e.exceptionCode.status).body(CommonResponse.CommonErrorResponse(e, e.detailMessages))
    }



}