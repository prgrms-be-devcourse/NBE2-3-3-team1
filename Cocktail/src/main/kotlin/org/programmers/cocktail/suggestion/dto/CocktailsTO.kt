package org.programmers.cocktail.suggestion.dto

import lombok.*

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
data class CocktailsTO(
    val id: Long? = null,
    val name: String? = null,
    val ingredients: String? = null,
    val description: String? = null,
    val recipes: String? = null,
    val category: String? = null,
    val alcoholic: String? = null,
    val imageUrl: String? = null,
    val hits: Long? = null,
    val likes: Long? = null
)