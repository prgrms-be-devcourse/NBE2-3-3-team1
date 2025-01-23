package org.programmers.cocktail.repository.comments

import org.programmers.cocktail.entity.Comments
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface CommentsRepositoryCustom {
    fun deleteCommentById(id: Long?): Boolean

    fun countTotalCommentsUntilYesterday(today: LocalDateTime?): Long?

    fun countCommentsList(today: LocalDateTime): List<Long?>

    fun searchByKeyword(keyword: String, pageable: Pageable): Page<Comments>
}

