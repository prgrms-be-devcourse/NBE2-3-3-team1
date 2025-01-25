package org.programmers.cocktail.list.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
class ListResponseTO(
    val name: String,
    val imageUrl: String,
    likes: Long?,
    updatedAt: LocalDateTime?
) {
    val id: Long? = null
    val likes: Long? = null
    val updatedAt: LocalDateTime? = null
}
