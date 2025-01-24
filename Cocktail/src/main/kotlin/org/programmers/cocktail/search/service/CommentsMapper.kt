package org.programmers.cocktail.search.service

import org.programmers.cocktail.entity.Comments
import org.programmers.cocktail.search.dto.CommentsTO
import org.springframework.stereotype.Service

@Service
object CommentsMapper {

    // Comments -> CommentsTO 변환
    fun convertToCommentsTO(comments: Comments?): CommentsTO {
        return CommentsTO(
            userId = comments?.users?.id,        // users 객체에서 id를 가져와서 userId로 설정
            cocktailId = comments?.cocktails?.id, // cocktails 객체에서 id를 가져와서 cocktailId로 설정
            userName = comments?.users?.name     // users 객체에서 name을 가져와서 userName으로 설정
        )
    }

    // CommentsTO -> Comments 변환
    fun convertToComments(commentsTO: CommentsTO?): Comments {
        val comments = Comments()

        // userId와 cocktailId로 users, cocktails 객체를 설정
        comments.users.id = commentsTO?.userId
        comments.cocktails.id = commentsTO?.cocktailId

        return comments
    }

    // Comments 리스트 -> CommentsTO 리스트 변환
    fun convertToCommentsTOList(commentsList: List<Comments?>): List<CommentsTO> {
        return commentsList.map { convertToCommentsTO(it) }
    }
}