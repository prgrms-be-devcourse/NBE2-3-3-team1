package org.programmers.cocktail.list.controller

import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.programmers.cocktail.list.service.ListService
import org.programmers.cocktail.repository.cocktails.CocktailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/popular")
class ListController {
    @Autowired
    var listService: ListService? = null

    @Autowired
    private val cocktailsRepository: CocktailsRepository? = null


    @GetMapping("")
    fun show(model: Model): String {
        val list = listService!!.showList()

        val topList = if (list.size > 10) list.subList(0, 10) else list

        model.addAttribute("list", topList)

        return "user/top"
    }

    @GetMapping("/detail/{id}")
    fun detail(@PathVariable id: Long, model: Model): String {
        // ID로 칵테일 정보를 가져옴
        val cocktail = listService!!.getCocktailById(id)

        // 칵테일 정보를 모델에 추가
        model.addAttribute("cocktailById", cocktail)

        // search3.html로 이동
        return "user/search3" // 상세 페이지를 위한 템플릿 이름
    }

    // 오류 페이지 처리
    @RequestMapping("/error")
    fun handleError(model: Model, request: HttpServletRequest): String {
        // 오류 상태 코드 확인
        val statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) as Int
        when (statusCode) {
            404 -> {
                model.addAttribute("error", "Page not found")
                return "error/404" // 404 오류 페이지로 이동
            }
            500 -> {
                model.addAttribute("error", "Internal Server Error")
                return "error/500" // 500 오류 페이지로 이동
            }
            else -> {
                model.addAttribute("error", "An unexpected error occurred")
                return "error/general" // 일반 오류 페이지로 이동
            }
        }
    }
}