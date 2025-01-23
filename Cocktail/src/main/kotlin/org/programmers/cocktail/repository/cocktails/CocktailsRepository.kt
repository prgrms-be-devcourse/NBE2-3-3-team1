package org.programmers.cocktail.repository.cocktails

import jakarta.persistence.LockModeType
import org.programmers.cocktail.entity.Cocktails
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.*

interface CocktailsRepository : JpaRepository<Cocktails?, Long?>, CocktailsRepositoryCustom {
    fun findAllByOrderByLikesDesc(): List<Cocktails?>?
    fun findAllByOrderByHitsDesc(): List<Cocktails?>?

    @Query("SELECT c FROM cocktails c WHERE c.name LIKE %:userInput% OR c.ingredients LIKE %:userInput%")
    fun findByNameOrIngredients(userInput: String?): List<Cocktails?>?

    //CocktailsService.updateCocktailHits 메서드 Pessimistic Lock 적용
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("Select c From cocktails c where c.id = :id")
    fun findByIdWithPessimisticLock(id: Long?): Optional<Cocktails?>?
}

