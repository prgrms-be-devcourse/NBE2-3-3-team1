package org.programmers.cocktail.suggestion.dto

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
data class Message(
    val role: String? = null,
    val content: String? = null
) {
}