package org.programmers.cocktail.search.dto

import lombok.*

data class CocktailLikesTO (
    val id: Long? = null,
    var userId: Long? = null,
    var cocktailId: Long? = null,
)
