package org.programmers.cocktail.admin.controller

import org.programmers.cocktail.admin.service.DashboardService
import org.programmers.cocktail.admin.service.HitsLogService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/admin")
class DashboardWebController(
    private val dashboardService: DashboardService,
    private val hitsLogService: HitsLogService
) {

    private val log: Logger = LoggerFactory.getLogger(DashboardWebController::class.java)

    /**
     * 대시보드 데이터를 반환합니다.
     */
    @GetMapping("/dashboard")
    fun dashboard(): ModelAndView {
        val mv = ModelAndView("/admin/dashboard")

        // 칵테일 Top 3 리스트 추가
        mv.addObject("list", dashboardService.cocktailsTopThreeByLikesDesc)

        // 사용자 통계
        val userCount = dashboardService.countByRoleUser()
        val yesterdayUsers = dashboardService.countTotalUserUntilYesterday() ?: 0
        val userGrowthRate = calculateGrowthRate(userCount.toDouble(), yesterdayUsers.toDouble())
        mv.addObject("userCount", userCount)
        mv.addObject("yesterdayUsers", yesterdayUsers)
        mv.addObject("userGrowthRate", userGrowthRate)
        mv.addObject("usersList", dashboardService.countUserTotalList())

        // 조회수 통계
        val totalHits = hitsLogService.todayLog
        val yesterdayHits = hitsLogService.yesterdayLog
        val hitsGrowthRate = calculateGrowthRate(totalHits.toDouble(), yesterdayHits.toDouble())
        mv.addObject("totalHits", totalHits)
        mv.addObject("yesterdayHits", yesterdayHits)
        mv.addObject("hitsGrowthRate", hitsGrowthRate)
        mv.addObject("HitsLog", hitsLogService.listLog)

        // 댓글 통계
        val commentCount = dashboardService.countComments()
        val yesterdayComments = dashboardService.countTotalCommentsUntilYesterday() ?: 0
        val commentGrowthRate = calculateGrowthRate(commentCount.toDouble(), yesterdayComments.toDouble())
        mv.addObject("commentCount", commentCount)
        mv.addObject("yesterdayComments", yesterdayComments)
        mv.addObject("commentGrowthRate", commentGrowthRate)
        mv.addObject("commentsList", dashboardService.countCommentsList())

        return mv
    }

    /**
     * 성장률 계산 함수
     */
    private fun calculateGrowthRate(current: Double, previous: Double): Double {
        return if (previous > 0) {
            String.format("%.2f", current / previous).toDouble()
        } else {
            0.0
        }
    }
}
