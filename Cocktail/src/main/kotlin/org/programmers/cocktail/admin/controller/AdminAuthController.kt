package org.programmers.cocktail.admin.controller

import lombok.RequiredArgsConstructor
import org.programmers.cocktail.admin.service.AdminAuthService
import org.programmers.cocktail.global.response.ApiResponse
import org.programmers.cocktail.global.response.ApiResponse.Companion.createSuccessWithNoData
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/authority")
@RequiredArgsConstructor
class AdminAuthController {
    private val adminAuthService: AdminAuthService? = null

    @PostMapping("/login")
    fun loginSubmit(
        @RequestParam email: String,
        @RequestParam password: String?
    ): ResponseEntity<ApiResponse<Any>> {
        adminAuthService!!.authenticate(email, password)
        return ResponseEntity.ok(createSuccessWithNoData())
    }

    @PostMapping("/join")
    fun joinSubmit(
        @RequestParam name: String?,
        @RequestParam email: String?,
        @RequestParam password: String?,
        @RequestParam confirmPassword: String?
    ): ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.ok().body(createSuccessWithNoData())
    }
}
