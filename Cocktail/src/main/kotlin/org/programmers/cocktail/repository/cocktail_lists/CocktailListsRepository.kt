package org.programmers.cocktail.repository.cocktail_lists

import jakarta.transaction.Transactional
import org.programmers.cocktail.entity.CocktailLists
import org.programmers.cocktail.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*


interface CocktailListsRepository : JpaRepository<CocktailLists?, Long?> {
    @Query(value = "select clist from cocktail_lists clist where clist.users.id = :userId and clist.cocktails.id = :cocktailId")
    fun findByUserIdAndCocktailId(userId: Long?, cocktailId: Long?): Optional<CocktailLists?>?

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "Delete from cocktail_lists clist where clist.users.id = :userId and clist.cocktails.id = :cocktailId")
    fun deleteByUserIdAndCocktailId(userId: Long?, cocktailId: Long?): Int

    fun deleteAllByUsers(users: Users?)
}
