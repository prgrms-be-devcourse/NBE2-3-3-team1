package org.programmers.cocktail.global.Utility

import org.programmers.cocktail.search.dto.UsersTO
import org.programmers.cocktail.search.service.UsersService
import org.springframework.beans.factory.annotation.Autowired

@org.springframework.stereotype.Component
class SearchUtils {
    @Autowired
    private val usersService: UsersService? = null

    fun searchUserByUserEmail(sessionValue: String?): UsersTO {
        // 유저 정보 가져올 수 없음(500반환)
        val nullHandledSessionValue = sessionValue?:throw RuntimeException("Login session cannot be found")
        val userInfo: UsersTO = usersService?.findByEmail(nullHandledSessionValue) ?: throw RuntimeException("UserService has no response")

        return userInfo
    }
}
