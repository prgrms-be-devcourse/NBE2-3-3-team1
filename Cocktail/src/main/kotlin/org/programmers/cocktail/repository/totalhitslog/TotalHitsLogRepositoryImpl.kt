package org.programmers.cocktail.repository.totalhitslog

import com.querydsl.jpa.impl.JPAQueryFactory
import org.programmers.cocktail.entity.QTotalHitsLog
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class TotalHitsLogRepositoryImpl(private val queryFactory: JPAQueryFactory) :
    TotalHitsLogRepositoryCustom {
    override fun getYesterdayLog(yesterday: LocalDateTime?): Long {
        val result = queryFactory
            .select(QTotalHitsLog.totalHitsLog.totalHits.sum())
            .from(QTotalHitsLog.totalHitsLog)
            .where(
                QTotalHitsLog.totalHitsLog.recordedAt.loe(yesterday)
            )
            .fetchOne()
        return result ?: 0
    }

    override fun getTodayLog(today: LocalDateTime?): Long {
        val result = queryFactory
            .select(QTotalHitsLog.totalHitsLog.totalHits.sum())
            .from(QTotalHitsLog.totalHitsLog)
            .where(
                QTotalHitsLog.totalHitsLog.recordedAt.loe(today)
            )
            .fetchOne()
        return result ?: 0
    }

    override fun getListLog(today: LocalDateTime): List<Long?> {
        val logs: MutableList<Long?> = ArrayList()
        for (i in 0..6) {
            logs.add(
                queryFactory
                    .select(QTotalHitsLog.totalHitsLog.totalHits.sum())
                    .from(QTotalHitsLog.totalHitsLog)
                    .where(
                        QTotalHitsLog.totalHitsLog.recordedAt.loe(
                            today.minusDays(6).plusDays(i.toLong()).withHour(23).withMinute(59)
                                .withSecond(59)
                        )
                    ).fetchOne()
            )
        }
        return logs
    }
}
