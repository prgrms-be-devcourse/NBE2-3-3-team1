package org.programmers.cocktail.admin.dto

data class DashboardCocktailRequest(
    val name: String,
    val imageUrl: String,
    val hits: Long,
    val likes: Long,
    val id: Long? = null
)