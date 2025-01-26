package org.programmers.cocktail.admin.controller

import org.programmers.cocktail.admin.dto.UserResponse
import org.programmers.cocktail.admin.service.AdminUserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/admin/users")
class AdminUserWebController(
    private val adminUserService: AdminUserService
) {

    private val log: Logger = LoggerFactory.getLogger(AdminUserWebController::class.java)

    @GetMapping("")
    fun adminUser(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ModelAndView {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        val userResponsePage: Page<UserResponse> =
            adminUserService.findAllByAuthoritiesRole("ROLE_USER", pageable)

        val totalPages = userResponsePage.totalPages
        val currentPage = userResponsePage.number + 1
        val maxPagesToShow = 10

        val startPage = if (totalPages <= maxPagesToShow) 1
        else maxOf(1, ((currentPage - 1) / maxPagesToShow) * maxPagesToShow + 1)
        val endPage = if (totalPages <= maxPagesToShow) totalPages
        else minOf(startPage + maxPagesToShow - 1, totalPages)

        return ModelAndView("admin/users").apply {
            addObject("users", userResponsePage.content)
            addObject("totalPages", totalPages)
            addObject("page", currentPage)
            addObject("startPage", startPage)
            addObject("endPage", endPage)
        }
    }

    @GetMapping("/search")
    fun searchUsers(
        @RequestParam("keyword", defaultValue = "") keyword: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ModelAndView {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        log.info("keyword: {}", keyword)

        val userPage = adminUserService.searchByKeyword(keyword, pageable)

        val totalPages = userPage.totalPages
        val currentPage = userPage.number + 1
        val maxPagesToShow = 10
        val startPage = if (totalPages <= maxPagesToShow) 1
        else maxOf(1, ((currentPage - 1) / maxPagesToShow) * maxPagesToShow + 1)
        val endPage = if (totalPages <= maxPagesToShow) totalPages
        else minOf(startPage + maxPagesToShow - 1, totalPages)

        return ModelAndView("admin/users").apply {
            addObject("users", userPage.content)
            addObject("totalPages", totalPages)
            addObject("page", currentPage)
            addObject("startPage", startPage)
            addObject("endPage", endPage)
            addObject("keyword", keyword)
        }
    }

}
