package org.programmers.cocktail.search.service

import org.programmers.cocktail.repository.users.UsersRepository
import org.programmers.cocktail.search.dto.UsersTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsersService {
    @Autowired
    private val usersRepository: UsersRepository? = null

    @Autowired
    private val usersMapper: UsersMapper? = null

    fun findByEmail(sessionEmail: String): UsersTO? {
        val userInfo = usersRepository?.findByEmail(sessionEmail)?.orElse(null)
        return userInfo?.let { usersMapper?.convertToUsersTO(it) } ?: throw RuntimeException("Failed to convert to UserTO")
    }
}
