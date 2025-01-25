package org.programmers.cocktail.login.dto

import lombok.Getter
import lombok.Setter
import lombok.ToString

@ToString
data class UserRegisterDto (
    var id: Long? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var gender: String? = null,
    var age: Int? = null
    ) {}