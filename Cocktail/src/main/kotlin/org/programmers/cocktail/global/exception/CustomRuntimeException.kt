package org.programmers.cocktail.global.exception

import lombok.Getter
import lombok.RequiredArgsConstructor

class CustomRuntimeException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)