package org.programmers.cocktail.admin.controller

import lombok.RequiredArgsConstructor
import org.programmers.cocktail.admin.service.AdminUserService
import org.programmers.cocktail.global.response.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
class AdminUserController {
    @Autowired
    private val adminUserService: AdminUserService? = null

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ApiResponse<Any>> {
        adminUserService!!.deleteById(id)
        return ResponseEntity.ok().body(ApiResponse.createSuccessWithNoData())
    }
}
