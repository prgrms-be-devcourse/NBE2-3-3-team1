package org.programmers.cocktail.search.service

import org.programmers.cocktail.repository.comments.CommentsRepository
import org.programmers.cocktail.search.dto.CommentsTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentsService {
    @Autowired
    var commentsRepository: CommentsRepository? = null

    @Autowired
    var commentsMapper: CommentsMapper? = null

    fun findByCocktailId(cocktailId: Long?): List<CommentsTO> {
        val commentsList = commentsRepository!!.findByCocktailId(cocktailId)

        if (commentsList!!.isEmpty()) {
            return emptyList()
        }

        val commentsTOList = commentsMapper!!.convertToCommentsTOList(commentsList)

        return commentsTOList
    }

    fun insertComments(commentsTO: CommentsTO?): Int {
        // TO->Entity 변환

        val comments = commentsMapper!!.convertToComments(commentsTO)
        try {
            commentsRepository!!.save(comments)
        } catch (e: Exception) {
            println("[에러]" + e.message)
            return FAIL
        }

        return SUCCESS
    }

    fun deleteById(commentsTO: CommentsTO): Int {
        val commentsId: Long = commentsTO.setId()

        val commentsDeleteResult =
            commentsRepository!!.deleteByIdWithReturnAffectedRowCount(commentsId)

        if (commentsDeleteResult == 0) {
            return FAIL
        }

        return SUCCESS
    }


    companion object {
        const val SUCCESS: Int = 1
        const val FAIL: Int = 0
    }
}
