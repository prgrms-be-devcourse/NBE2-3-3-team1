package org.programmers.cocktail.repository.comments

import jakarta.transaction.Transactional
import org.programmers.cocktail.entity.Comments
import org.programmers.cocktail.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface CommentsRepository : JpaRepository<Comments?, Long?>,
    CommentsRepositoryCustom {

    @Query(value = "select cmt from comments cmt where cmt.cocktails.id = :cocktailId")
    fun findByCocktailId(cocktailId: Long?): List<Comments?>?

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("Delete from comments cmt where cmt.id = :commentId")
    fun deleteByIdWithReturnAffectedRowCount(commentId: Long?): Int

    fun deleteAllByUsers(users: Users?)

    @Query("SELECT c FROM comments c WHERE c.updatedAt > :lastSyncTime")
    fun findByUpdatedAtAfter(@Param("lastSyncTime") lastSyncTime: LocalDateTime?): List<Comments?>?

    @Query("SELECT MAX(c.updatedAt) FROM comments c")
    fun findMostRecentUpdatedAt(): LocalDateTime?
}
