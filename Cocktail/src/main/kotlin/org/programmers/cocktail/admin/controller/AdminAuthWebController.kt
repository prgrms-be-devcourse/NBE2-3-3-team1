package org.programmers.cocktail.admin.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminAuthWebController {
    @GetMapping("/login")
    fun login(): String {
        return "admin/admin_signin"
    }
}
