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
        mv: ModelAndView,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ModelAndView {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        val commentResponses = adminCommentService.findAllComments(pageable)
        mv.addObject("comments", commentResponses.content)
        mv.addObject("totalPages", commentResponses.totalPages)
        mv.addObject("page", commentResponses.number + 1)
        mv.viewName = "admin/comments"
        return mv
    }

    @GetMapping("/search")
    fun searchComments(
        @RequestParam("keyword") keyword: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        mv: ModelAndView
    ): ModelAndView {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        mv.addObject("keyword", keyword)

        val comments = adminCommentService.searchByKeyword(keyword, pageable)


        mv.addObject("comments", comments.content)
        mv.addObject("totalPages", comments.totalPages)
        mv.addObject("page", comments.number + 1)

        mv.viewName = "admin/comments"
        return mv
    }
}
