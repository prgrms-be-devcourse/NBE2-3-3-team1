package org.programmers.cocktail.admin.service

import jakarta.transaction.Transactional
import org.programmers.cocktail.admin.dto.UserResponse
import org.programmers.cocktail.entity.Users
import org.programmers.cocktail.exception.ErrorCode
import org.programmers.cocktail.global.exception.CustomDataAccessException
import org.programmers.cocktail.global.exception.CustomRuntimeException
import org.programmers.cocktail.global.exception.NotFoundException
import org.programmers.cocktail.repository.authorities.AuthoritiesRepository
import org.programmers.cocktail.repository.cocktail_likes.CocktailLikesRepository
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepository
import org.programmers.cocktail.repository.comments.CommentsRepository
import org.programmers.cocktail.repository.users.UsersRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AdminUserService(
    private val usersRepository: UsersRepository,
    private val commentsRepository: CommentsRepository,
    private val cocktailListsRepository: CocktailListsRepository,
    private val cocktailLikesRepository: CocktailLikesRepository,
    private val authoritiesRepository: AuthoritiesRepository
) {

    private val log: Logger = LoggerFactory.getLogger(AdminUserService::class.java)

    fun findAllByAuthoritiesRole(role: String?, pageable: Pageable): Page<UserResponse> {
        val users: Page<Users?> = usersRepository.findAllByAuthorities_Role(role, pageable)
        return users.map { user -> UserResponse.from(user) }
    }






    @Transactional
    fun deleteById(id: Long) {
        val user = usersRepository.findById(id).orElseThrow {
            NotFoundException(ErrorCode.USER_NOT_FOUND)
        }

        try {
            cocktailLikesRepository.deleteAllByUsers(user)
            commentsRepository.deleteAllByUsers(user)
            cocktailListsRepository.deleteAllByUsers(user)
            authoritiesRepository.deleteAllByUsers(user)
            if (user != null) {
                usersRepository.delete(user)
            }
        } catch (e: CustomDataAccessException) {
            throw CustomDataAccessException(ErrorCode.DATABASE_ERROR.message, e)
        } catch (e: RuntimeException) {
            throw CustomRuntimeException(ErrorCode.INTERNAL_SERVER_ERROR.message, e)
        }
    }

    fun searchByKeyword(keyword: String, pageable: Pageable): Page<UserResponse> =
        usersRepository.searchByKeyword(keyword, pageable).map { UserResponse.from(it) }

}
