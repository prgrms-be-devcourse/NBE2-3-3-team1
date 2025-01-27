package org.programmers.cocktail.admin.service

import org.programmers.cocktail.repository.totalhitslog.TotalHitsLogRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class HitsLogService(private val totalHitsLogRepository: TotalHitsLogRepository) {
    val yesterdayLog: Long
        get() {
            val yesterday =
                LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59)

            return totalHitsLogRepository.getYesterdayLog(yesterday)
        }

    val todayLog: Long
        get() {
            val today = LocalDateTime.now()

            return totalHitsLogRepository.getTodayLog(today)
        }

    val listLog: List<Long?>
        get() {
            val today = LocalDateTime.now()

            return totalHitsLogRepository.getListLog(today)
        }
}
