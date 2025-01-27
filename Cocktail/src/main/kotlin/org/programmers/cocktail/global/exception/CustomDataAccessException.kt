package org.programmers.cocktail.global.exception

class CustomDataAccessException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)