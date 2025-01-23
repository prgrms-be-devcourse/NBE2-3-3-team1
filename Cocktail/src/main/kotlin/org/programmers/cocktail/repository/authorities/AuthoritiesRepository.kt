package org.programmers.cocktail.repository.authorities

import org.programmers.cocktail.entity.Authorities
import org.programmers.cocktail.entity.Users
import org.springframework.data.jpa.repository.JpaRepository

interface AuthoritiesRepository : JpaRepository<Authorities?, Long?>, AuthoritiesRepositoryCustom {
    fun countByRole(role: String?): Int

    fun deleteAllByUsers(users: Users?)
}
