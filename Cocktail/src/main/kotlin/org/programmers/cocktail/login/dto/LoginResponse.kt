package org.programmers.cocktail.login.dto

import lombok.Getter
import lombok.Setter

@Setter
@Getter
data class LoginResponse(
    var accessToken: String? = null,
    var refreshToken: String? = null,
    var image: String? = null,
    var nickname: String? = null,
    var email: String? = null
) {}