package org.programmers.cocktail.admin.service

import jakarta.transaction.Transactional
import org.programmers.cocktail.admin.dto.CommentResponse
import org.programmers.cocktail.entity.Comments
import org.programmers.cocktail.exception.ErrorCode
import org.programmers.cocktail.global.exception.BadRequestException
import org.programmers.cocktail.global.exception.CustomDataAccessException
import org.programmers.cocktail.global.exception.CustomRuntimeException
import org.programmers.cocktail.global.exception.NotFoundException
import org.programmers.cocktail.repository.comments.CommentsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AdminCommentService(
    private val commentsRepository: CommentsRepository
) {
    private val log: Logger = LoggerFactory.getLogger(AdminCommentService::class.java)

    fun findAllComments(pageable: Pageable): Page<CommentResponse> {
        val comments = commentsRepository.findAll(pageable)
        log.info(comments.toString())
        return comments.map { CommentResponse(it) }
    }

    @Transactional
    fun deleteById(id: Long?) {
        if (id == null || id <= 0) {
            throw BadRequestException(ErrorCode.BAD_REQUEST)
        }
        try {
            val isDeleted = commentsRepository.deleteCommentById(id)
            if (!isDeleted) {
                throw NotFoundException(ErrorCode.USER_NOT_FOUND)
            }
        } catch (e: CustomDataAccessException) {
            throw CustomDataAccessException(ErrorCode.DATABASE_ERROR.message, e)
        } catch (e: RuntimeException) {
            throw CustomRuntimeException(ErrorCode.INTERNAL_SERVER_ERROR.message, e)
        }
    }

    fun searchByKeyword(keyword: String, pageable: Pageable): Page<CommentResponse> {
        val comments = commentsRepository.searchByKeyword(keyword, pageable)
        return comments.map { CommentResponse(it) }
    }
}
