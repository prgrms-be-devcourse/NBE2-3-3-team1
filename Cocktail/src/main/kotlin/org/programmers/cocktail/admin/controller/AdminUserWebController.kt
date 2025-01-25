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

        return ModelAndView("admin/users").apply {
            addObject("users", userResponsePage.content)
            addObject("totalPages", userResponsePage.totalPages)
            addObject("page", userResponsePage.number + 1)
        }
    }

    @GetMapping("/search")
    fun searchUsers(
        @RequestParam("keyword") keyword: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        model: Model
    ): String {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        log.info("keyword: {}", keyword)
        model.addAttribute("keyword", keyword)

        val userPage = adminUserService.searchByKeyword(keyword, pageable)

        model.addAttribute("users", userPage.content)
        model.addAttribute("totalPages", userPage.totalPages)
        model.addAttribute("page", userPage.number + 1)

        return "admin/users"
    }
}
