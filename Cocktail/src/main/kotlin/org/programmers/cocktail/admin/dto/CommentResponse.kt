package org.programmers.cocktail.admin.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import org.programmers.cocktail.entity.Comments
import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val content: String,
    val createdAt: LocalDateTime,
    val authorName: String,
    val cocktailName: String
) {
    constructor(comment: Comments?) : this(
        id = comment?.id ?: 0L,
        content = comment?.content ?: "",
        createdAt = comment?.createdAt ?: LocalDateTime.now(),
        authorName = comment?.users?.name ?: "",
        cocktailName = comment?.cocktails?.name ?: ""
    )
}
