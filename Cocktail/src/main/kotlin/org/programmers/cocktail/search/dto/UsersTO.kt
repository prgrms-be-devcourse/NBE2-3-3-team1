package org.programmers.cocktail.search.dto

import lombok.*

data class UsersTO (
    val id: Long? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
)
