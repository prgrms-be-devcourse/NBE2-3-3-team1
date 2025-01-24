package org.programmers.cocktail.search.service

import org.programmers.cocktail.entity.CocktailLists
import org.programmers.cocktail.search.dto.CocktailListsTO
import org.springframework.stereotype.Service

@Service
object CocktailListsMapper1 {

    // CocktailLists -> CocktailListsTO 변환 매핑
    fun convertToCocktailsListsTO(cocktailLists: CocktailLists?): CocktailListsTO {
        return CocktailListsTO(
            userId = cocktailLists?.users?.id,    // users 객체에서 id를 가져와서 userId로 설정
            cocktailId = cocktailLists?.cocktails?.id // cocktails 객체에서 id를 가져와서 cocktailId로 설정
        )
    }

    // CocktailLikesTO -> CocktailLikes 변환
    fun convertToCocktailLists(cocktailListsTO: CocktailListsTO?): CocktailLists {

        var cocktailLists : CocktailLists = CocktailLists()
        cocktailLists.users.id = cocktailListsTO?.userId
        cocktailLists.cocktails.id = cocktailListsTO?.cocktailId

        return cocktailLists
    }
}