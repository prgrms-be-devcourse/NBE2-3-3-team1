package org.programmers.cocktail.global.exception

import lombok.Getter
import lombok.RequiredArgsConstructor

@RequiredArgsConstructor
@Getter
class CustomDataAccessException(message: String?, cause: Throwable?) :
    RuntimeException(message, cause)
