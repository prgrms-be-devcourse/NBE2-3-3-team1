package org.programmers.cocktail.repository.totalhitslog

import org.programmers.cocktail.entity.TotalHitsLog
import org.springframework.data.jpa.repository.JpaRepository

interface TotalHitsLogRepository : JpaRepository<TotalHitsLog?, Long?>, TotalHitsLogRepositoryCustom
