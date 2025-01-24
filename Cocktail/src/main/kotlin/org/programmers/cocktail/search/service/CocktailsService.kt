package org.programmers.cocktail.search.service

import jakarta.transaction.Transactional
import org.programmers.cocktail.repository.cocktails.CocktailsRepository
import org.programmers.cocktail.search.dto.CocktailsTO
import org.programmers.cocktail.search.enums.FindAllByOrderDescActionType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CocktailsService {
    @Autowired
    private val cocktailsRepository: CocktailsRepository? = null

    @Autowired
    private val cocktailsMapper: CocktailsMapper? = null

    fun findAllByOrderDesc(findAllByOrderDescActionType: FindAllByOrderDescActionType): List<CocktailsTO> {
        val cocktailsDescList =
            if (findAllByOrderDescActionType == FindAllByOrderDescActionType.ORDER_BY_LIKES) {
                cocktailsRepository?.findAllByOrderByLikesDesc()
            } else {
                cocktailsRepository?.findAllByOrderByHitsDesc()
            } ?: throw RuntimeException("Failed to get Sorted Cocktail list from Database ")

        return cocktailsMapper?.convertToCocktailsTOList(cocktailsDescList) ?: throw RuntimeException("UserService has no response")
    }

    fun findByNameContaining(keyword: String?): List<CocktailsTO> {
        val cocktailSearchList = cocktailsRepository!!.findByNameOrIngredients(keyword)

        if (!cocktailSearchList!!.isEmpty()) {
            return cocktailsMapper!!.convertToCocktailsTOList(cocktailSearchList)
        }

        return emptyList()
    }

    fun insertNewCocktailDBbyList(cocktailsTOList: List<CocktailsTO?>): Int {
        try {
            //List<TO>->List<Entity> 변환
            val cocktailsList = cocktailsMapper!!.convertToCocktailsList(cocktailsTOList)
            cocktailsRepository!!.saveAll(cocktailsList)
            return SUCCESS //저장성공
        } catch (e: Exception) {
            println("[저장실패] : " + e.message)
            return FAIL //저장실패
        }
    }

    fun findById(cocktailId: Long): CocktailsTO {
        val cocktails = cocktailsRepository!!.findById(cocktailId).orElse(null)
        val cocktailsTO = cocktailsMapper!!.convertToCocktailsTO(cocktails)

        return cocktailsTO
    }

    @Transactional
    fun updateCocktailHits(cocktailsTO: CocktailsTO): Int {
        // Pessimistic Lock 적용

        val cocktailsOptional =
            cocktailsRepository!!.findByIdWithPessimisticLock(cocktailsTO.id)

        if (!cocktailsOptional!!.isPresent) {
            return FAIL // 칵테일 불러오기 실패
        }

        val cocktails = cocktailsOptional.get()
        cocktails.hits += 1

        cocktailsRepository.flush()

        return SUCCESS
    }

    fun updateCocktailLikesCount(cocktailsTO: CocktailsTO): Int {
        val cocktails = cocktailsRepository?.findById(cocktailsTO?.id ?: return FAIL)?.orElse(null) ?: return FAIL

        cocktails.setLikes(cocktailsTO.getLikes())

        cocktailsRepository.flush()

        println(cocktails)

        return SUCCESS
    }

    companion object {
        const val SUCCESS: Int = 1
        const val FAIL: Int = 0
    }
}
