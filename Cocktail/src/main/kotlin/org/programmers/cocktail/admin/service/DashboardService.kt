package org.programmers.cocktail.admin.service

import org.programmers.cocktail.admin.dto.DashboardCocktailResponse
import org.programmers.cocktail.entity.Cocktails
import org.programmers.cocktail.entity.Users
import org.programmers.cocktail.repository.authorities.AuthoritiesRepository
import org.programmers.cocktail.repository.cocktails.CocktailsRepository
import org.programmers.cocktail.repository.comments.CommentsRepository
import org.programmers.cocktail.repository.users.UsersRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class DashboardService(
    private val usersRepository: UsersRepository,
    private val cocktailsRepository: CocktailsRepository,
    private val authoritiesRepository: AuthoritiesRepository,
    private val commentsRepository: CommentsRepository
) {

    private val log: Logger = LoggerFactory.getLogger(DashboardService::class.java)

    val cocktailsTopThreeByLikesDesc: List<DashboardCocktailResponse>
        get() = cocktailsRepository.findAllByOrderByLikesDesc()
            .take(3)
            .mapNotNull { cocktail ->
                cocktail?.let {
                    DashboardCocktailResponse(
                        name = it.name,
                        imageUrl = it.image_url?: "",
                        hits = it.hits,
                        likes = it.likes,
                        comments = it.comments.size.toLong()
                    )
                }
            }


    fun countByRoleUser(): Long {
        val countByRole = authoritiesRepository.countByRole("ROLE_USER").toLong()
        log.info("countByRole: {}", countByRole)
        return countByRole
    }

    fun countUserTotalList(): List<Long> {
        return authoritiesRepository.countUserTotalList(LocalDateTime.now())
            .mapNotNull { it }
    }


    fun countComments(): Long =
        commentsRepository.findAll().size.toLong()

    fun getUserById(id: Long): Users? =
        usersRepository.findById(id).orElse(null)


    fun countTotalUserUntilYesterday(): Long? {
        log.info("localDateTime: {}", LocalDateTime.now())
        val countTotalUserUntilYesterday =
            authoritiesRepository.countTotalUserUntilYesterday("ROLE_USER", LocalDateTime.now())
        log.info("countTotalUserUntilYesterday: {}", countTotalUserUntilYesterday)
        return countTotalUserUntilYesterday
    }

    fun countTotalCommentsUntilYesterday(): Long? =
        commentsRepository.countTotalCommentsUntilYesterday(LocalDateTime.now())

    fun countCommentsList(): List<Long> =
        commentsRepository.countCommentsList(LocalDateTime.now())
            .mapNotNull { it }

}
