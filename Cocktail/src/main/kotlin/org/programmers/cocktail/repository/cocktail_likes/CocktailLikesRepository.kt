package org.programmers.cocktail.repository.cocktail_likes

import jakarta.transaction.Transactional
import org.programmers.cocktail.entity.CocktailLikes
import org.programmers.cocktail.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.util.*

interface CocktailLikesRepository : JpaRepository<CocktailLikes?, Long?> {
    @Query(value = "select Count(clikes) from cocktails_likes clikes where clikes.cocktails.id= :cocktailId")
    fun countCocktailLikesById(cocktailId: Long?): Long?

    @Query(value = "select clikes from cocktails_likes clikes where clikes.users.id = :userId and clikes.cocktails.id = :cocktailId")
    fun findByUserIdAndCocktailId(userId: Long?, cocktailId: Long?): Optional<CocktailLikes?>?

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "Delete from cocktails_likes clikes where clikes.users.id = :userId and clikes.cocktails.id = :cocktailId")
    fun deleteByUserIdAndCocktailId(userId: Long?, cocktailId: Long?): Int

    fun deleteAllByUsers(users: Users?)

    @Query("SELECT cl FROM cocktails_likes cl WHERE cl.updatedAt > :lastSyncTime")
    fun findByUpdatedAtAfter(@Param("lastSyncTime") lastSyncTime: LocalDateTime?): List<CocktailLikes?>?

    @Query("SELECT MAX(cl.updatedAt) FROM cocktails_likes cl")
    fun findMostRecentUpdatedAt(): LocalDateTime?
}
