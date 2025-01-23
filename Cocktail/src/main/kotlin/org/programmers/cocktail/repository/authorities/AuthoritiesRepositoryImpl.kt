package org.programmers.cocktail.repository.authorities

import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.extern.slf4j.Slf4j
import org.programmers.cocktail.entity.QAuthorities
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class AuthoritiesRepositoryImpl(private val queryFactory: JPAQueryFactory) :

    AuthoritiesRepositoryCustom {

    private val log: Logger = LoggerFactory.getLogger(AuthoritiesRepositoryImpl::class.java)
    override fun countTotalUserUntilYesterday(authority: String, to: LocalDateTime): Long? {
        val authorities = QAuthorities.authorities
        log.info("Parameter to: {}", to)
        val until = to.minusDays(1).withHour(23).withMinute(59).withSecond(59)
        log.info("Calculated until: {}", until)
        return queryFactory
            .select(authorities.id.count())
            .from(authorities)
            .where(
                authorities.role.eq(authority)
                    .and(authorities.createdAt.loe(until))
            )
            .fetchOne()
    }

    override fun countUserTotalList(today: LocalDateTime): List<Long?> {
        val authorities = QAuthorities.authorities
        val lists: MutableList<Long?> = ArrayList()
        for (i in 0..6) {
            lists.add(
                queryFactory
                    .select(authorities.id.count())
                    .from(authorities)
                    .where(
                        authorities.role.eq("ROLE_USER")
                            .and(
                                authorities.createdAt.loe(
                                    today
                                        .minusDays(6).withHour(23).withMinute(59).withSecond(59)
                                        .plusDays(i.toLong())
                                )
                            )
                    )
                    .fetchOne()
            )
        }
        return lists
    }
}
