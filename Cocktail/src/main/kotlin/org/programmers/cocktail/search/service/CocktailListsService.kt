package org.programmers.cocktail.search.service

import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepository
import org.programmers.cocktail.search.dto.CocktailListsTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CocktailListsService {
    @Autowired
    private val cocktailListsRepository: CocktailListsRepository? = null


    @Autowired
    private val cocktailListsMapper: CocktailListsMapper? = null

    fun findByUserIdAndCocktailId(userId: Long?, cocktailId: Long?): Int {
        val cocktailListsOptional =
            cocktailListsRepository!!.findByUserIdAndCocktailId(userId, cocktailId)

        if (!cocktailListsOptional!!.isPresent) {
            return FAIL
        }
        return SUCCESS
    }

    fun insertCocktailList(cocktailListsTO: CocktailListsTO?): Int {
        // TO->Entity 변환

        val cocktailLists = cocktailListsMapper!!.convertToCocktailLists(cocktailListsTO)
        try {
            cocktailListsRepository!!.save(cocktailLists)
        } catch (e: Exception) {
            println("[에러]" + e.message)
            return FAIL
        }

        return SUCCESS
    }

    fun deleteCocktailList(cocktailListsTO: CocktailListsTO): Int {
        val cocktailListDeleteResult = cocktailListsRepository!!.deleteByUserIdAndCocktailId(
            cocktailListsTO.getUserId(),
            cocktailListsTO.getCocktailId()
        )

        if (cocktailListDeleteResult == 0) {
            // 삭제된 행이 없는 경우
            return FAIL
        }

        return SUCCESS
    }

    companion object {
        const val SUCCESS: Int = 1
        const val FAIL: Int = 0
    }
}
