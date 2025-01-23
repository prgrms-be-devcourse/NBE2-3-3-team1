package org.programmers.cocktail.repository.users

import org.programmers.cocktail.entity.Users
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface UsersRepositoryCustom {
    fun searchByKeyword(keyword: String, pageable: Pageable): Page<Users>
}