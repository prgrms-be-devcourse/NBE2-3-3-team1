package org.programmers.cocktail.repository.totalhitslog

import java.time.LocalDateTime

interface TotalHitsLogRepositoryCustom {
    fun getYesterdayLog(yesterday: LocalDateTime?): Long

    fun getTodayLog(today: LocalDateTime?): Long

    fun getListLog(today: LocalDateTime): List<Long?>
}
