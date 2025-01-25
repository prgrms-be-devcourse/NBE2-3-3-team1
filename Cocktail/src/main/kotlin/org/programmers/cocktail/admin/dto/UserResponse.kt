package org.programmers.cocktail.admin.dto

import org.programmers.cocktail.entity.Users
import java.time.LocalDateTime

data class UserResponse(
    val id: Long = 0L,
    val name: String = "",
    val email: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun from(users: Users?): UserResponse {
            return UserResponse(
                id = users?.id ?: 0L,
                name = users?.name ?: "",
                email = users?.email ?: "",
                createdAt = users?.createdAt ?: LocalDateTime.now(),
                updatedAt = users?.updatedAt ?: LocalDateTime.now()
            )
        }
    }
}
