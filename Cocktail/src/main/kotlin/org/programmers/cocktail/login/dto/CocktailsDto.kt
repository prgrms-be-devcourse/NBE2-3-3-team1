package org.programmers.cocktail.login.dto

import lombok.ToString

@ToString
data class CocktailsDto (
    var id: Long? = null,
    var name: String? = null,
    var ingredients: String? = null,
    var recipes: String? = null,
    var category: String? = null,
    var alcoholic: String? = null,
    var image_url: String? = null,
    var hits: Long? = null,
    var likes: Long? = null
) {}