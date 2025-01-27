package org.programmers.cocktail.global.exception

import jakarta.persistence.EntityNotFoundException
import org.programmers.cocktail.exception.ErrorCode
import org.programmers.cocktail.global.response.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import java.io.IOException

/**
 * 전역 예외 처리 클래스
 *
 * 이 클래스는 애플리케이션 내에서 발생하는 다양한 예외를 전역적으로 처리하여
 * 클라이언트에게 적절한 HTTP 응답을 제공합니다.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    private val log: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    /**
     * 에러 응답을 생성합니다.
     *
     * @param status HTTP 상태 코드
     * @param message 에러 메시지
     * @return 에러 정보를 포함한 ResponseEntity
     */
    private fun createErrorResponse(
        status: HttpStatus,
        message: String
    ): ResponseEntity<ApiResponse<Any>> {
        log.error("[Error] $message")
        return ResponseEntity.status(status).body(ApiResponse.createError(message))
    }

    /**
     * 잘못된 경로로 인한 404 에러를 처리합니다.
     *
     * @param e NoHandlerFoundException 예외 객체
     * @return 404 상태 코드와 메시지를 포함한 ResponseEntity
     */
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException): ResponseEntity<ApiResponse<Any>> {
        return createErrorResponse(HttpStatus.NOT_FOUND, e.message ?: "No handler found")
    }

    /**
     * 지원되지 않는 HTTP 메서드로 인한 405 에러를 처리합니다.
     *
     * @param e HttpRequestMethodNotSupportedException 예외 객체
     * @return 405 상태 코드와 메시지를 포함한 ResponseEntity
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<ApiResponse<Any>> {
        return createErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, e.message ?: "Method not allowed")
    }

    /**
     * 잘못된 요청 파라미터로 인한 400 에러를 처리합니다.
     *
     * @param e IllegalArgumentException 예외 객체
     * @return 400 상태 코드와 메시지를 포함한 ResponseEntity
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ApiResponse<Any>> {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.message ?: "Bad request")
    }

    /**
     * 커스텀 예외 BadRequestException을 처리합니다.
     *
     * @param e BadRequestException 예외 객체
     * @return 해당 에러 코드와 메시지를 포함한 ResponseEntity
     */
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException): ResponseEntity<ApiResponse<Any>> {
        val httpStatus = e.errorCode.let { HttpStatus.resolve(it.status) } ?: HttpStatus.INTERNAL_SERVER_ERROR
        val message = e.errorCode.message ?: "An unexpected error occurred"
        return createErrorResponse(httpStatus, message)
    }

    /**
     * 유효성 검증 실패로 인한 400 에러를 처리합니다.
     *
     * @param e MethodArgumentNotValidException 예외 객체
     * @return 유효성 검증 에러 메시지를 포함한 ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Any>> {
        val errorMessage = e.bindingResult.allErrors.joinToString("\n") { it.defaultMessage ?: "Validation error" }
        return createErrorResponse(HttpStatus.BAD_REQUEST, errorMessage)
    }

    /**
     * 데이터 조회 실패로 인한 404 에러를 처리합니다.
     *
     * @param e EntityNotFoundException 예외 객체
     * @return 404 상태 코드와 메시지를 포함한 ResponseEntity
     */
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<ApiResponse<Any>> {
        return createErrorResponse(HttpStatus.NOT_FOUND, e.message ?: "Entity not found")
    }

    /**
     * 입출력 예외로 인한 500 에러를 처리합니다.
     *
     * @param e IOException 예외 객체
     * @return 500 상태 코드와 메시지를 포함한 ResponseEntity
     */
    @ExceptionHandler(IOException::class)
    fun handleIOException(e: IOException): ResponseEntity<ApiResponse<Any>> {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.message ?: "I/O error")
    }

    /**
     * 데이터 액세스 중 발생한 예외를 처리합니다.
     *
     * @param e DataAccessException 예외 객체
     * @return 500 상태 코드와 메시지를 포함한 ResponseEntity
     */
    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(e: DataAccessException): ResponseEntity<ApiResponse<Any>> {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.message ?: "Database error")
    }

    /**
     * 위에서 처리되지 않은 모든 예외를 처리합니다.
     *
     * @param e Exception 예외 객체
     * @return 500 상태 코드와 기본 메시지를 포함한 ResponseEntity
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> {
        log.error("[Exception] message: {}, cause: {}", e.message, e.cause)
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error")
    }

    // 세션 인증 실패시 에러
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException() : ResponseEntity<ApiResponse<Any>> {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "UnAuthorizedException Error")
    }
}
