package org.programmers.cocktail.search.dto

import java.time.LocalDateTime

data class CommentsTO (
    var id: Long? = null,
    var content: String? = null,
    var userId: Long? = null,
    var userName: String? = null,
    var cocktailId: Long? = null,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null
)
