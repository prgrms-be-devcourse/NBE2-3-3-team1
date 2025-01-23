package org.programmers.cocktail.search.dto

import lombok.*
import java.time.LocalDateTime

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
class CommentsTO {
    private val id: Long? = null
    private val content: String? = null
    private val userId: Long? = null
    private val userName: String? = null
    private val cocktailId: Long? = null
    private val createdAt: LocalDateTime? = null
    private val updatedAt: LocalDateTime? = null
}
