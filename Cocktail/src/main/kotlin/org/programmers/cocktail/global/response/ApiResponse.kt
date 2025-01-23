package org.programmers.cocktail.global.response

import com.fasterxml.jackson.annotation.JsonInclude
import org.programmers.cocktail.global.constant.ResponseStatus

/**
 * 데이터 공통 반환 형식 정의 클래스
 *
 * @param T 제네릭 데이터 타입
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값은 JSON에서 제외
data class ApiResponse<T>(
    val status: String,
    val message: String?,
    val data: T? = null // 기본값 null 설정
) {
    companion object {
        /**
         * 반환 데이터 없는 성공 response
         *
         * @return 성공 상태의 ApiResponse
         */
        fun createSuccessWithNoData(): ApiResponse<Any> {
            return ApiResponse(
                status = ResponseStatus.SUCCESS.getMsg(),
                message = null,
                data = null
            )
        }

        /**
         * 반환 데이터 있는 성공 response
         *
         * @param T 데이터 타입
         * @param data 성공 데이터
         * @return 성공 상태의 ApiResponse
         */
        fun <T> createSuccess(data: T): ApiResponse<T> {
            return ApiResponse(
                status = ResponseStatus.SUCCESS.getMsg(),
                message = null,
                data = data
            )
        }

        /**
         * 에러 response
         *
         * @param msg 에러 메시지
         * @return 에러 상태의 ApiResponse
         */
        fun createError(msg: String?): ApiResponse<Any> {
            return ApiResponse(
                status = ResponseStatus.ERROR.getMsg(),
                message = msg,
                data = null
            )
        }

        /**
         * 에러 response(msg 직접 지정)
         *
         * @param msg 사용자 정의 에러 메시지
         * @return 에러 상태의 ApiResponse
         */
        fun createErrorWithMsg(msg: String?): ApiResponse<Any> {
            return ApiResponse(
                status = ResponseStatus.ERROR.getMsg(),
                message = msg,
                data = null
            )
        }
    }
}
