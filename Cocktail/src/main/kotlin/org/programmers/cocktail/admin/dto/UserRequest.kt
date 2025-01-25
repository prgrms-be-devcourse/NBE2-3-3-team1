package org.programmers.cocktail.admin.dto

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

data class UserRequest(
    val email: String,
    val password: String,
    val username: String? = null
)