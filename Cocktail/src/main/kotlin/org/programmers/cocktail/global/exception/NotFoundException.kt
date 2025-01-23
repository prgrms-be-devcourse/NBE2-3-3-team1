package org.programmers.cocktail.global.exception

import lombok.Getter
import lombok.RequiredArgsConstructor
import org.programmers.cocktail.exception.ErrorCode

@RequiredArgsConstructor
@Getter
class NotFoundException : RuntimeException() {
    private val errorCode: ErrorCode? = null
}
