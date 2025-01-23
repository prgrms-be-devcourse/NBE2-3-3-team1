package org.programmers.cocktail.repository.cocktail_likes

import org.programmers.cocktail.entity.CocktailLikes
import org.springframework.data.jpa.repository.JpaRepository

interface CocktailLikesRepositoryCustom : JpaRepository<CocktailLikes?, Long?>
