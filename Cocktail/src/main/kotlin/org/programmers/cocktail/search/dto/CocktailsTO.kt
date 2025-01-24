package org.programmers.cocktail.search.dto

import lombok.*

data class CocktailsTO(
    var id: Long ? = null,
    var name: String ? = null,
    var ingredients: String ? = null,
    var recipes: String ? = null,
    var category: String? = null,
    var alcoholic: String? = null,
    var image_url: String? = null,
    var hits: Long = 0L,
    var likes: Long = 0L
)
