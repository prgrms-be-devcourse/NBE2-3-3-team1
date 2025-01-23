package org.programmers.cocktail.repository.authorities

import java.time.LocalDateTime

interface AuthoritiesRepositoryCustom {
    fun countTotalUserUntilYesterday(authority: String, to: LocalDateTime): Long?

    fun countUserTotalList(today: LocalDateTime): List<Long?>
}
