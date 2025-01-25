package org.programmers.cocktail.search.controller

import org.programmers.cocktail.search.dto.CocktailsTO
import org.programmers.cocktail.search.service.CocktailExternalApiService
import org.programmers.cocktail.search.service.CocktailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class SearchHtmlController {
    @Autowired
    var cocktailsService: CocktailsService? = null

    @Autowired
    var cocktailExternalApiService: CocktailExternalApiService? = null

    // 검색페이지 반환
    @RequestMapping("/search")
    fun getCocktailSearchPage(model: Model?): String {
        // Word Cloud 생성후 전송
//        ElasticsearchClientComponent elasticSearchClientComponent = new ElasticsearchClientComponent();
//        ElasticsearchQueryBuilder queryBuilder = new ElasticsearchQueryBuilder(elasticSearchClientComponent);
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            model.addAttribute("male20sWordCloudMap", objectMapper.writeValueAsString(queryBuilder.fetchWordCloudData("Male", "20")));
//            model.addAttribute("male30sWordCloudMap", objectMapper.writeValueAsString(queryBuilder.fetchWordCloudData("Male", "30")));
//            model.addAttribute("male40sWordCloudMap", objectMapper.writeValueAsString(queryBuilder.fetchWordCloudData("Male", "40")));
//            model.addAttribute("female20sWordCloudMap", objectMapper.writeValueAsString(queryBuilder.fetchWordCloudData("Female", "20")));
//            model.addAttribute("female30sWordCloudMap", objectMapper.writeValueAsString(queryBuilder.fetchWordCloudData("Female", "30")));
//            model.addAttribute("female40sWordCloudMap", objectMapper.writeValueAsString(queryBuilder.fetchWordCloudData("Female", "40")));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        return "user/search1"
    }

    // 검색결과 반환
    @RequestMapping("/search/cocktailresults")
    fun getCocktailSearchResultPage(@RequestParam userInput: String, model: Model): String {
        // 검색결과 설정

        val keyword = userInput

        //1. DB에 검색결과 있는지 확인
        var cocktailSearchList: List<CocktailsTO?> =
            cocktailsService!!.findByNameContaining(keyword)

        if (!cocktailSearchList.isEmpty()) {
            //DB에 결과가 있는 경우 반환
            println("Data exists in Local DB")
            model.addAttribute("cocktailSearchList", cocktailSearchList)
            return "user/search2"
        }
        //2. (DB에 결과가 없는경우) API에서 검색
        println("No data in local DB. Fetching Data from Extenal API...")

        // 2-1) 외부 API에서 가져온 cocktail 정보를 DB에 저장
        cocktailSearchList = cocktailExternalApiService!!.fetchCocktailData(keyword)

        val cocktailInsertByListResult =
            cocktailsService!!.insertNewCocktailDBbyList(cocktailSearchList)

        if (cocktailInsertByListResult == 0) {
            // todo 데이터 삽입 실패시 프론트에 alert 띄울지 고민
            println("[에러] New Cocktail Data Insertion Failed")
        }

        // 2-2) DB에 저장된 데이터를 가져와서 반환
        cocktailSearchList = cocktailsService!!.findByNameContaining(keyword)

        if (cocktailSearchList == null || cocktailSearchList.isEmpty()) {
            println("No matching results found in Local DB and Extenal API")
            model.addAttribute("cocktailSearchList", emptyList<Any>()) // 비어있는 리스트 전달
            return "user/search2"
        }

        model.addAttribute("cocktailSearchList", cocktailSearchList)

        return "user/search2"
    }


    @RequestMapping("/recommend")
    fun getRecommendCocktailPage(): String {
        // 추천칵테일 페이지 반환
        return "user/suggestion"
    }


    // 메인페이지 반환
    @RequestMapping("/")
    fun getMainCocktailPage(model: Model): String {
        val cocktailSearchList = cocktailExternalApiService!!.fetchCocktailData()

        // 1. 랜덤 칵테일 불러오기 실패시
        if (cocktailSearchList == null || cocktailSearchList.isEmpty()) {
            println("Failed to get random response from Extenal API")
            model.addAttribute("cocktailSearchList", emptyList<Any>()) // 비어있는 리스트 전달
            return "user/main"
        }

        // 2. 랜덤 칵테일 불러오기 성공시 -> 반환
        model.addAttribute("cocktailSearchList", cocktailSearchList)

        return "user/main"
    }

    // 칵테일 상세 페이지 반환
    @RequestMapping("/search/cocktails/{cocktailId}")
    fun getCocktailInfoById(@PathVariable cocktailId: String, model: Model): String {
        // 특정 칵테일 상세페이지 조회시 해당 칵테일 hit 증가

        val cocktailsTO = CocktailsTO()
        cocktailsTO.id = cocktailId.toLong()

        // SUCCESS: 1, FAIL: 0
        val cocktailHitsUpdateResult = cocktailsService!!.updateCocktailHits(cocktailsTO)

        if (cocktailHitsUpdateResult == 0) {
            println("[에러] Cocktail Hits Update Failed")
        }

        val cocktailsById = cocktailsService!!.findById(cocktailId.toLong())
        model.addAttribute("cocktailById", cocktailsById)

        // todo 프론트페이지에서 cocktailById가 null인 경우 alert 띄우도록 처리 필요
        // todo 리스트 상세페이지 구현되면 상세페이지 반환하도록 설정
        return "user/search3"
    }
}
