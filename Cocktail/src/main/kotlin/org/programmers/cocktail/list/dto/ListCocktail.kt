package org.programmers.cocktail.list.dto

import lombok.*

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
data class ListCocktail (
    val id: Long? = null,
    val name: String? = null,
    val image_url: String? = null,
    val likes: Long? = 0
)
