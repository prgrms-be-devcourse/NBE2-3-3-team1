package org.programmers.cocktail.admin.controller

import org.programmers.cocktail.admin.service.AdminCommentService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/admin/comments")
class AdminCommentWebController(private val adminCommentService: AdminCommentService) {
    @GetMapping("")
    fun admin_comment(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ModelAndView {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        val commentResponses = adminCommentService.findAllComments(pageable)

        val totalPages = commentResponses.totalPages
        val currentPage = commentResponses.number + 1
        val maxPagesToShow = 10
        val startPage = if (totalPages <= maxPagesToShow) 1
        else maxOf(1, ((currentPage - 1) / maxPagesToShow) * maxPagesToShow + 1)
        val endPage = if (totalPages <= maxPagesToShow) totalPages
        else minOf(startPage + maxPagesToShow - 1, totalPages)

        return ModelAndView("admin/comments").apply {
            addObject("comments", commentResponses.content)
            addObject("totalPages", totalPages)
            addObject("page", currentPage)
            addObject("startPage", startPage)
            addObject("endPage", endPage)
        }
    }

    @GetMapping("/search")
    fun searchComments(
        @RequestParam("keyword", defaultValue = "") keyword: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ModelAndView {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        val comments = adminCommentService.searchByKeyword(keyword, pageable)

        val totalPages = comments.totalPages
        val currentPage = comments.number + 1
        val maxPagesToShow = 10
        val startPage = if (totalPages <= maxPagesToShow) 1
        else maxOf(1, ((currentPage - 1) / maxPagesToShow) * maxPagesToShow + 1)
        val endPage = if (totalPages <= maxPagesToShow) totalPages
        else minOf(startPage + maxPagesToShow - 1, totalPages)

        return ModelAndView("admin/comments").apply {
            addObject("comments", comments.content)
            addObject("totalPages", totalPages)
            addObject("page", currentPage)
            addObject("startPage", startPage)
            addObject("endPage", endPage)
            addObject("keyword", keyword)
        }
    }
}
