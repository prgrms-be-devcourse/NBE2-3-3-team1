package org.programmers.cocktail.global.constant

import lombok.Getter

/**
 * 데이터 Response status 속성 구성 enum 클래스
 */
@Getter
enum class ResponseStatus(private val msg: String) {
    SUCCESS("성공"),
    ERROR("오류");

    fun getMsg(): String = this.msg
}