package org.programmers.cocktail.search.service

import jakarta.transaction.Transactional
import org.programmers.cocktail.global.Utility.SearchUtils
import org.programmers.cocktail.repository.cocktail_likes.CocktailLikesRepository
import org.programmers.cocktail.search.dto.CocktailLikesTO
import org.programmers.cocktail.search.dto.CocktailsTO
import org.programmers.cocktail.search.enums.UpdateLikesInfoByUserActionType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CocktailLikesService {
    @Autowired
    private val cocktailLikesRepository: CocktailLikesRepository? = null

    @Autowired
    private val cocktailLikesMapper: CocktailLikesMapper? = null

    @Autowired
    private val searchUtils: SearchUtils? = null

    @Autowired
    private val cocktailsService: CocktailsService? = null

    @Transactional
    fun updateLikesInfoByUser(
        sessionValue: String?,
        cocktailId: String,
        updateLikesInfoByUserActionType: UpdateLikesInfoByUserActionType
    ): Long? {
        // 1. cocktail_likes에서 user_id, cocktail_id 삭제
        val cocktailLikesTO = CocktailLikesTO()
        cocktailLikesTO.userId = searchUtils!!.searchUserByUserEmail(sessionValue).id
        cocktailLikesTO.cocktailId = cocktailId.toLong()

        // SUCCESS: 1, FAIL: 0
        if (updateLikesInfoByUserActionType == UpdateLikesInfoByUserActionType.ADD) {
            val cocktailLikesInsertResult = insertCocktailLikes(cocktailLikesTO)

            if (cocktailLikesInsertResult == 0) {
                throw RuntimeException("Failed to add a new like to the cocktail_likes table") // DB추가 실패(500반환)
            }
        } else {
            val cocktailLikesDeleteResult = deleteCocktailLikes(cocktailLikesTO)

            if (cocktailLikesDeleteResult == 0) {
                throw RuntimeException("Failed to delete a like in cocktail_likes table") // DB삭제 실패(500반환)
            }
        }

        // 2. cocktailId에 해당하는 cocktailsLikes 값 가져오기
        val cocktailLikesCountById = countCocktailLikesById(cocktailLikesTO) ?: throw RuntimeException("Failed to get cocktailLikesById")

        // 3. cocktails테이블에 cocktailsLikes 값 업데이트
        val cocktailsTO = CocktailsTO()
        cocktailsTO.id = cocktailId.toLong()
        cocktailsTO.likes = cocktailLikesCountById

        val cocktailLikesCountUpdateResult =
            cocktailsService?.updateCocktailLikesCount(cocktailsTO) ?: throw RuntimeException("Failed to update Cocktail Likes Count(cocktailService is null)")

        // SUCCESS: 1, FAIL: 0
        if (cocktailLikesCountUpdateResult == 0) {
            throw RuntimeException("Failed to update likes in cocktails table") // 칵테일 좋아요 업데이트 실패(500반환)
        }

        return cocktailLikesCountById
    }


    fun findByUserIdAndCocktailId(userId: Long?, cocktailId: Long?): Int {
        val cocktailLikesOptional =
            cocktailLikesRepository!!.findByUserIdAndCocktailId(userId, cocktailId)

        if (!cocktailLikesOptional!!.isPresent) {
            return FAIL
        }
        return SUCCESS
    }

    fun insertCocktailLikes(cocktailLikesTO: CocktailLikesTO?): Int {
        // TO->Entity 변환

        val cocktailLikes = cocktailLikesMapper!!.convertToCocktailLikes(cocktailLikesTO)
        try {
            cocktailLikesRepository!!.save(cocktailLikes)
        } catch (e: Exception) {
            println("[에러]" + e.message)
            return FAIL
        }

        return SUCCESS
    }

    fun deleteCocktailLikes(cocktailLikesTO: CocktailLikesTO): Int {
        val cocktailLikesDeleteResult = cocktailLikesRepository!!.deleteByUserIdAndCocktailId(
            cocktailLikesTO.userId,
            cocktailLikesTO.cocktailId
        )



        if (cocktailLikesDeleteResult == 0) {
            // 삭제된 행이 없는 경우
            return FAIL
        }

        return SUCCESS
    }

    fun countCocktailLikesById(cocktailLikesTO: CocktailLikesTO): Long? {
        //todo 예외처리 -> 에러 발생시 어떻게 처리할지 체크
        // 1) cocktailLikesTO에 cocktailid가 null일때
        // 2)

        val cocktailLikesCountById =
            cocktailLikesRepository!!.countCocktailLikesById(cocktailLikesTO.cocktailId)

        return cocktailLikesCountById
    }

    companion object {
        const val SUCCESS: Int = 1
        const val FAIL: Int = 0
    }
}
