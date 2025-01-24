package org.programmers.cocktail.search.controller

import org.programmers.cocktail.global.Utility.SearchUtils
import org.programmers.cocktail.global.annotation.RequireLogin
import org.programmers.cocktail.search.dto.CocktailListsTO
import org.programmers.cocktail.search.dto.CocktailsTO
import org.programmers.cocktail.search.dto.CommentsTO
import org.programmers.cocktail.search.enums.FindAllByOrderDescActionType
import org.programmers.cocktail.search.enums.UpdateLikesInfoByUserActionType
import org.programmers.cocktail.search.service.CocktailLikesService
import org.programmers.cocktail.search.service.CocktailListsService
import org.programmers.cocktail.search.service.CocktailsService
import org.programmers.cocktail.search.service.CommentsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.support.SessionStatus
import java.util.*

@RestController
@RequestMapping("/api")
class SearchController {
    @Autowired
    private val cocktailsService: CocktailsService? = null

    @Autowired
    private val cocktailListsService: CocktailListsService? = null

    @Autowired
    private val cocktailLikesService: CocktailLikesService? = null

    @Autowired
    private val commentsService: CommentsService? = null

    @Autowired
    private val searchUtils: SearchUtils? = null

    @GetMapping("/cocktails/top/{criteria}")
    fun getTopHitsCocktails(@PathVariable criteria: String): ResponseEntity<List<CocktailsTO?>> {
        var cocktailsDescTOList: List<CocktailsTO?> = Collections.EMPTY_LIST as List<CocktailsTO?>

        if (criteria == "likes") {
            cocktailsDescTOList =
                cocktailsService!!.findAllByOrderDesc(FindAllByOrderDescActionType.ORDER_BY_LIKES)
        } else if (criteria == "hits") {
            cocktailsDescTOList =
                cocktailsService!!.findAllByOrderDesc(FindAllByOrderDescActionType.ORDER_BY_HITS)
        }

        val top5cocktails = if (cocktailsDescTOList.size > 5) cocktailsDescTOList.subList(
            0,
            5
        ) else cocktailsDescTOList

        if (top5cocktails.isEmpty()) {
            throw RuntimeException("Failed to get Top Likes Cocktails") // TopLikesCocktail 가져오기 실패(500반환)
        }

        return ResponseEntity.ok(top5cocktails)
    }

    @GetMapping("/favorites/cocktails/{cocktailId}")
    @RequireLogin
    fun isFavoritedByUser(
        @SessionAttribute(value = "semail", required = false) sessionValue: String?,
        @PathVariable cocktailId: String,
        sessionStatus: SessionStatus?
    ): ResponseEntity<Int> {
        val PRESENT = 1
        val ABSENT = 0

        //userid, cocktailid가 cocktail_lists에 존재하는지 확인( SUCCESS: 1, FAIL: 0 )
        val isCocktailListsPresent = cocktailListsService!!.findByUserIdAndCocktailId(
            searchUtils!!.searchUserByUserEmail(sessionValue).id, cocktailId.toLong()
        )

        if (isCocktailListsPresent == 0) {
            return ResponseEntity.ok(ABSENT) // 즐겨찾기 조회 성공(즐겨찾기 없는 경우 - 200 반환)
        }

        return ResponseEntity.ok(PRESENT) // 즐겨찾기 조회 성공(즐겨찾기 있는 경우 - 200반환)
    }

    @PostMapping("/favorites/cocktails/{cocktailId}")
    @RequireLogin
    fun addFavoritesByUser(
        @SessionAttribute(
            value = "semail",
            required = false
        ) sessionValue: String?, @PathVariable cocktailId: String
    ): ResponseEntity<Void> {
        // cocktail_lists에 user_id, cocktail_id 저장

        val cocktailListsTO = CocktailListsTO()
        cocktailListsTO.userId = searchUtils!!.searchUserByUserEmail(sessionValue).id
        cocktailListsTO.cocktailId = cocktailId.toLong()

        // SUCCESS: 1, FAIL: 0
        val cocktailListInsertResult = cocktailListsService!!.insertCocktailList(cocktailListsTO)

        if (cocktailListInsertResult == 0) {
            throw RuntimeException("Failed to add a new favorite to the cocktail_lists table") // DB추가 실패(500반환)
        }

        return ResponseEntity.noContent().build() //DB추가 성공(204반환)
    }

    @DeleteMapping("/favorites/cocktails/{cocktailId}")
    @RequireLogin
    fun deleteFavoritesByUser(
        @SessionAttribute(
            value = "semail",
            required = false
        ) sessionValue: String?, @PathVariable cocktailId: String
    ): ResponseEntity<Void> {
        // cocktail_lists에서 user_id, cocktail_id 삭제

        val cocktailListsTO = CocktailListsTO()
        cocktailListsTO.userId = searchUtils!!.searchUserByUserEmail(sessionValue).id
        cocktailListsTO.cocktailId = cocktailId.toLong()

        // SUCCESS: 1, FAIL: 0
        val cocktailListDeleteResult = cocktailListsService!!.deleteCocktailList(cocktailListsTO)

        if (cocktailListDeleteResult == 0) {
            throw RuntimeException("Failed to add a new favorite to the cocktail_lists table") // DB삭제 실패(500반환)
        }

        return ResponseEntity.noContent().build() //DB삭제 성공
    }

    @GetMapping("/likes/cocktails/{cocktailId}")
    @RequireLogin
    fun isLikedByUser(
        @SessionAttribute(value = "semail", required = false) sessionValue: String?,
        @PathVariable cocktailId: String
    ): ResponseEntity<Int> {
        val PRESENT = 1
        val ABSENT = 0

        // userid, cocktailid가 cocktail_likes에 존재하는지 확인(SUCCESS: 1, FAIL: 0)
        val isCocktailLikesPresent = cocktailLikesService!!.findByUserIdAndCocktailId(
            searchUtils!!.searchUserByUserEmail(sessionValue).id, cocktailId.toLong()
        )

        if (isCocktailLikesPresent == 0) {
            return ResponseEntity.ok(ABSENT) // 좋아요 조회 성공(좋아요 없는 경우 - 200 반환)
        }

        return ResponseEntity.ok(PRESENT) // 좋아요 조회 성공(좋아요 있는 경우 - 200반환)
    }

    @PostMapping("/likes/cocktails/{cocktailId}")
    @RequireLogin
    fun addLikesByUser(
        @SessionAttribute(value = "semail", required = false) sessionValue: String?,
        @PathVariable cocktailId: String
    ): ResponseEntity<Long> {
        return ResponseEntity.ok(
            cocktailLikesService!!.updateLikesInfoByUser(
                sessionValue,
                cocktailId,
                UpdateLikesInfoByUserActionType.ADD
            )
        ) //DB추가 성공(200반환, 좋아요갯수 반환)
    }

    @DeleteMapping("/likes/cocktails/{cocktailId}")
    @RequireLogin
    fun deleteLikesByUser(
        @SessionAttribute(
            value = "semail",
            required = false
        ) sessionValue: String?, @PathVariable cocktailId: String
    ): ResponseEntity<Long> {
        return ResponseEntity.ok(
            cocktailLikesService!!.updateLikesInfoByUser(
                sessionValue,
                cocktailId,
                UpdateLikesInfoByUserActionType.DELETE
            )
        ) //DB삭제 성공(200반환, 좋아요 갯수 반환)
    }

    @GetMapping("/reviews/cocktails/{cocktailId}")
    fun loadCocktailComments(@PathVariable cocktailId: String): ResponseEntity<List<CommentsTO>> {
        val commentsTOList = commentsService!!.findByCocktailId(cocktailId.toLong())

        println("commentsTOList: $commentsTOList")
        if (commentsTOList.isEmpty() || commentsTOList == null) {
            println("commentsTOListisempty")
            return ResponseEntity.noContent().build() // 상태코드 204 전송
        }

        return ResponseEntity.ok(commentsTOList)
    }

    @PostMapping("/reviews/cocktails/{cocktailId}")
    @RequireLogin
    fun registerCocktailComments(
        @SessionAttribute(value = "semail", required = false) sessionValue: String?,
        @PathVariable cocktailId: String,
        @RequestBody commentsTOFromClient: CommentsTO
    ): ResponseEntity<Void> {
        val commentsTO = CommentsTO()
        commentsTO.content = commentsTOFromClient.content
        commentsTO.userId = searchUtils!!.searchUserByUserEmail(sessionValue).id
        commentsTO.cocktailId = cocktailId.toLong()

        // SUCCESS: 1, FAIL: 0
        val commentsInsertResult = commentsService!!.insertComments(commentsTO)

        if (commentsInsertResult == 0) {
            throw RuntimeException("Failed to add a new Comment to the comments table") // DB추가 실패(500반환)
        }

        return ResponseEntity.noContent().build() // DB추가 성공(204반환)
    }

    @DeleteMapping("/reviews/cocktails/{reviewId}")
    @RequireLogin
    fun deleteCocktailComments(
        @SessionAttribute(
            value = "semail",
            required = false
        ) sessionValue: String?, @PathVariable reviewId: String
    ): ResponseEntity<Void> {
        val commentsTO = CommentsTO()
        commentsTO.id = reviewId.toLong()

        val commentsDeleteResult = commentsService!!.deleteById(commentsTO)

        // SUCCESS: 1, FAIL: 0
        if (commentsDeleteResult == 0) {
            throw RuntimeException("Failed to delete a Comment in the comments table") // DB삭제 실패(500반환)
        }
        return ResponseEntity.noContent().build() // DB삭제 성공(204반환)
    }
}
