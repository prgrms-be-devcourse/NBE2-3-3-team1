package org.programmers.cocktail.global.exception

import org.programmers.cocktail.exception.ErrorCode

class NotFoundException(
    private val errorCode: ErrorCode
) : RuntimeException(errorCode.message)