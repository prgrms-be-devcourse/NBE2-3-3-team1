package org.programmers.cocktail.global.Utility

import org.programmers.cocktail.search.dto.UsersTO

@org.springframework.stereotype.Component
class SearchUtils {
    @Autowired
    private val usersService: UsersService? = null

    fun searchUserByUserEmail(sessionValue: String?): UsersTO {
        val userInfo: UsersTO = usersService.findByEmail(sessionValue)
            ?: // 유저 정보 가져올 수 없음(500반환)
            throw java.lang.RuntimeException("User not found")

        return userInfo
    }
}
