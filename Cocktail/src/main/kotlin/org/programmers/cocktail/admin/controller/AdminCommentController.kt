package org.programmers.cocktail.admin.controller

import lombok.RequiredArgsConstructor
import org.programmers.cocktail.admin.service.AdminCommentService
import org.programmers.cocktail.global.response.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
class AdminCommentController {
    @Autowired
    private val adminCommentService: AdminCommentService? = null

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: Long?): ResponseEntity<ApiResponse<Any>> {
        adminCommentService!!.deleteById(id)
        return ResponseEntity.ok().body(ApiResponse.createSuccessWithNoData())
    }
}
