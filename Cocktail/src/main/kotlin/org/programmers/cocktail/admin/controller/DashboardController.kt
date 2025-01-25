package org.programmers.cocktail.admin.controller

import lombok.extern.slf4j.Slf4j
import org.programmers.cocktail.admin.service.DashboardService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Slf4j
@RequestMapping("api/admin")
class DashboardController(private val dashboardService: DashboardService)
