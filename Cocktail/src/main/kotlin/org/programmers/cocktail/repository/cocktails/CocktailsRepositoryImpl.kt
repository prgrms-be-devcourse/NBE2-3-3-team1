package org.programmers.cocktail.repository.cocktails

import com.querydsl.jpa.impl.JPAQueryFactory
import org.programmers.cocktail.entity.QCocktails
import org.springframework.stereotype.Repository

@Repository
class CocktailsRepositoryImpl(private val queryFactory: JPAQueryFactory) :
    CocktailsRepositoryCustom {
    override val totalHits: Long
        get() {
            val cocktails = QCocktails.cocktails
            val sum = queryFactory
                .select(cocktails.hits.sum())
                .from(cocktails)
                .fetchOne()

            return sum ?: 0L
        }
}

