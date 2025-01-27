package org.programmers.cocktail.search.service

import com.fasterxml.jackson.databind.JsonNode
import org.programmers.cocktail.entity.Cocktails
import org.programmers.cocktail.search.dto.CocktailsTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class CocktailExternalApiService {
    @Autowired
    private val restTemplate: RestTemplate? = null

    @Autowired
    private val cocktailsMapper: CocktailsMapper? = null

    // 1. Cocktail 검색용
    fun fetchCocktailData(cocktailName: String): List<CocktailsTO?> {
        // TheCocktailDB 호출 url - "Search cocktail by name" method

        val url =
            "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=$cocktailName"

        return parseJsonToCocktailsTOList(url)
    }

    // 2. 메인 페이지 출력용
    fun fetchCocktailData(): List<CocktailsTO?> {
        // TheCocktailDB 호출 url - "Lookup a random cocktail" method

        val url = "https://www.thecocktaildb.com/api/json/v1/1/random.php"

        return parseJsonToCocktailsTOList(url)
    }

    fun parseJsonToCocktailsTOList(url: String): List<CocktailsTO?> {
        val response = restTemplate!!.getForEntity(
            url,
            JsonNode::class.java
        )

        // JSON 데이터 가져오기
        val drinksNode = response.body!!["drinks"]
        if (drinksNode == null || drinksNode.isEmpty) {
            // 외부 API에 검색결과 없는 경우 빈 리스트 반환
            return emptyList<CocktailsTO?>().toMutableList()
        }

        // 검색된 모든 칵테일 처리
        var cocktails: MutableList<CocktailsTO?> = ArrayList()
        for (drinkNode in drinksNode) {
            val name = drinkNode["strDrink"].asText()
            val ingredientsBuilder = StringBuilder()
            val recipes = drinkNode["strInstructions"].asText()
            val category = drinkNode["strCategory"].asText()
            val alcoholic = drinkNode["strAlcoholic"].asText()
            val image_url = drinkNode["strDrinkThumb"].asText()
            // 재료와 측정값 결합
            for (i in 1..15) {
                val ingredientNode = drinkNode["strIngredient$i"]
                val measureNode = drinkNode["strMeasure$i"]
                if (ingredientNode == null || ingredientNode.isNull) {
                    break // 재료가 없으면 중단
                }
                val ingredient = ingredientNode.asText()
                val measure =
                    if (measureNode != null && !measureNode.isNull) measureNode.asText() else ""
                ingredientsBuilder.append(ingredient).append(" ").append(measure).append(", ")
            }

            // 마지막 쉼표 제거
            val ingredients = ingredientsBuilder.toString().replace(", $".toRegex(), "")

            // ProcessedCocktail 객체 생성
            val cocktailsTO : CocktailsTO = CocktailsTO(name=name, ingredients=ingredients, recipes = recipes,
                category=category, alcoholic = alcoholic, image_url = image_url)

            cocktails.add(
                cocktailsTO
            )
        }
        return cocktails
    }
}