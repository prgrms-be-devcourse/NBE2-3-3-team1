package org.programmers.cocktail.suggestion.service

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.programmers.cocktail.suggestion.dto.CocktailsTO

@Service
class CocktailExternalApiImageURL {
    @Autowired
    private val restTemplate: RestTemplate? = null
    fun getCocktailImageURL(cocktailName: String): String? {
        // API 호출
        val url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=$cocktailName"
        val response = restTemplate!!.getForEntity(url, JsonNode::class.java)

        // JSON 데이터 가져오기
        val drinksNode = response.body?.get("drinks")
        if (drinksNode == null || drinksNode.isEmpty) {
            // 외부 API에 검색결과 없는 경우 빈 리스트 반환
            return ""
        }

        // 검색된 모든 칵테일 처리
        val cocktails: MutableList<CocktailsTO> = ArrayList()
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
                val measure = if (measureNode != null && !measureNode.isNull) measureNode.asText() else ""
                ingredientsBuilder.append(ingredient).append(" ").append(measure).append(", ")
            }

            // 마지막 쉼표 제거
            val ingredients = ingredientsBuilder.toString().replace(", $".toRegex(), "")

            // ProcessedCocktail 객체 생성
            cocktails.add(CocktailsTO(0L, name, ingredients, "", recipes, category, alcoholic, image_url, 0L, 0L))
        }
        return cocktails[0].imageUrl
    }
}