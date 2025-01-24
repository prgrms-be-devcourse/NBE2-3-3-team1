package org.programmers.cocktail.search.service

import org.programmers.cocktail.entity.CocktailLikes
import org.programmers.cocktail.search.dto.CocktailLikesTO
import org.springframework.stereotype.Service

@Service
object CocktailLikesMapper {

    // CocktailLikes -> CocktailLikesTO 변환
    fun convertToCocktailsLikesTO(cocktailLikes: CocktailLikes?): CocktailLikesTO {
        return CocktailLikesTO(
            userId = cocktailLikes?.users?.id,    // users 객체에서 id를 가져와서 userId로 설정
            cocktailId = cocktailLikes?.cocktails?.id // cocktails 객체에서 id를 가져와서 cocktailId로 설정
        )
    }

    // CocktailLikesTO -> CocktailLikes 변환
    fun convertToCocktailLikes(cocktailLikesTO: CocktailLikesTO?): CocktailLikes {

        var cocktailLikes : CocktailLikes = CocktailLikes()
        cocktailLikes.users.id = cocktailLikesTO?.userId
        cocktailLikes.cocktails.id = cocktailLikesTO?.cocktailId

        return cocktailLikes
    }
}
