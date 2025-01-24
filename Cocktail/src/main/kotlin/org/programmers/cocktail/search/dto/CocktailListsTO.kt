package org.programmers.cocktail.search.dto

import lombok.*


data class CocktailListsTO (
    var id: Long? = null,
    var userId: Long? = null,
    var cocktailId: Long? = null
)
