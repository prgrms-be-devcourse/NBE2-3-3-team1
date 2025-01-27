package org.programmers.cocktail.admin.dto

import org.programmers.cocktail.entity.Cocktails

data class DashboardCocktailResponse(
    val id: Long = 0L,
    val name: String = "",
    val imageUrl: String = "",
    val hits: Long = 0L,
    val likes: Long = 0L,
    val comments: Long = 0L
) {
    companion object {
        fun from(cocktails: Cocktails?): DashboardCocktailResponse {
            return DashboardCocktailResponse(
                id = cocktails?.id ?: 0L,
                name = cocktails?.name ?: "",
                imageUrl = cocktails?.image_url ?: "",
                hits = cocktails?.hits ?: 0L,
                likes = cocktails?.likes ?: 0L,
                comments = cocktails?.comments?.size?.toLong() ?: 0L
            )
        }
    }
}
