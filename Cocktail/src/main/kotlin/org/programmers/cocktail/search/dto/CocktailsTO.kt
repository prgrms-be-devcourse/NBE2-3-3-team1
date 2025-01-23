package org.programmers.cocktail.search.dto

import lombok.*

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
class CocktailsTO {
    private val id: Long? = null
    private val name: String? = null
    private val ingredients: String? = null
    private val recipes: String? = null
    private val category: String? = null
    private val alcoholic: String? = null
    private val image_url: String? = null
    private val hits: Long? = null
    private val likes: Long? = null
}
