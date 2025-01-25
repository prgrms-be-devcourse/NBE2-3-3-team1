package org.programmers.cocktail.admin.service

import org.programmers.cocktail.entity.TotalHitsLog
import org.programmers.cocktail.repository.cocktails.CocktailsRepository
import org.programmers.cocktail.repository.totalhitslog.TotalHitsLogRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TotalHitsScheduler(
    private val cocktailsRepository: CocktailsRepository,
    private val totalHitsLogRepository: TotalHitsLogRepository
) {
    @Scheduled(cron = "0 0 0 * * *")
    fun recordTotalHits() {
        val totalHits = cocktailsRepository.totalHits
        val log = TotalHitsLog(totalHits, LocalDateTime.now())

        totalHitsLogRepository.save(log)
    }
}
