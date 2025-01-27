package org.programmers.cocktail.global.exception

import org.programmers.cocktail.exception.ErrorCode

class BadRequestException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)