package org.programmers.cocktail.repository.comments

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.programmers.cocktail.entity.Comments
import org.programmers.cocktail.entity.QComments
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

class CommentsRepositoryImpl(private val queryFactory: JPAQueryFactory) : CommentsRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null


    override fun deleteCommentById(id: Long?): Boolean {
        val comments = QComments.comments

        val deletedRows = queryFactory
            .delete(comments)
            .where(comments.id.eq(id))
            .execute()

        return deletedRows > 0
    }

    override fun countTotalCommentsUntilYesterday(today: LocalDateTime?): Long? {
        val comments = QComments.comments

        val yesterday = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59)

        return queryFactory
            .select(comments.count())
            .from(comments)
            .where(
                comments.updatedAt.loe(yesterday)
            )
            .fetchOne()
    }

    override fun countCommentsList(today: LocalDateTime): List<Long?> {
        val comments = QComments.comments

        val lists: MutableList<Long?> = ArrayList()
        for (i in 0..6) {
            lists.add(
                queryFactory
                    .select(comments.count())
                    .from(comments)
                    .where(
                        comments.updatedAt.loe(
                            today
                                .minusDays(6)
                                .withHour(23)
                                .withMinute(59)
                                .withSecond(59)
                                .plusDays(i.toLong())
                        )
                    )
                    .fetchOne()
            )
        }

        return lists
    }

    override fun searchByKeyword(keyword: String, pageable: Pageable): Page<Comments> {
        val jpql = "select c from comments c where lower(c.content) like :keyword"

        val comments = entityManager!!.createQuery(
            jpql,
            Comments::class.java
        )
            .setParameter("keyword", "%" + keyword.lowercase(Locale.getDefault()) + "%")
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList


        val countJpql = "SELECT COUNT(c) FROM comments c WHERE LOWER(c.content) LIKE :keyword"
        val total = entityManager.createQuery(countJpql, Long::class.java)
            .setParameter("keyword", "%" + keyword.lowercase(Locale.getDefault()) + "%")
            .singleResult

        return PageImpl(comments, pageable, total)
    }
}
