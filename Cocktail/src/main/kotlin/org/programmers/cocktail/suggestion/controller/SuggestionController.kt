package org.programmers.cocktail.suggestion.controller


import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.programmers.cocktail.suggestion.dto.ChatGPTRequest
import org.programmers.cocktail.suggestion.dto.ChatGPTResponse
import org.programmers.cocktail.suggestion.service.CocktailExternalApiImageURL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestTemplate

@Controller
@RequestMapping("/api/suggestion")
class SuggestionController {
    @Value("\${openai.model}")
    private val model: String? = null

    @Value("\${openai.api.url}")
    private val apiURL: String? = null

    @Autowired
    private val template: RestTemplate? = null

    @Autowired
    var cocktailExternalApiImageURL: CocktailExternalApiImageURL? = null

    @GetMapping("")
    fun suggestion(): String {
        return "user/suggestion"
    }

    @RequestMapping("/detail")
    @Throws(Exception::class)
    fun getSuggestion(@RequestParam("query") query: String, modelObj: Model): String {
        // Query 분석 및 추천 로직
        val answers = query.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val answer1 = answers[0]
        val answer2 = answers[1]
        var prompt = ""
        prompt += "suggest one cocktail which is $answer1 and $answer2. Send me with only JSON format(don't write ```json and depth is less than 2) which only contains name, description, ingredient, instruction, imageURL also each information is going to be json index and do not change the spelling(do not add 's' or something else)"
        println(model)
        println(apiURL)
        val request = ChatGPTRequest(model, prompt)
        val chatGPTResponse = template!!.postForObject(
            apiURL!!, request,
            ChatGPTResponse::class.java
        )

        val jsonParser = JSONParser()

        //3. To Object
        val obj = jsonParser.parse(chatGPTResponse?.choices?.get(0)?.message?.content ?: null)

        //4. To JsonObject
        val jsonObj = obj as JSONObject

        //print
        println(jsonObj["name"]) //sim
        println(jsonObj["description"])
        println(jsonObj["ingredient"])
        println(jsonObj["instruction"])
        val keyword = jsonObj["name"].toString()
        val cocktailImageURL = cocktailExternalApiImageURL!!.getCocktailImageURL(keyword)

        // Model에 데이터 추가
        modelObj.addAttribute("cocktailName", jsonObj["name"])
        modelObj.addAttribute("cocktailDescription", jsonObj["description"])
        modelObj.addAttribute("cocktailIngredient", jsonObj["ingredient"])
        modelObj.addAttribute("cocktailInstruction", jsonObj["instruction"])
        modelObj.addAttribute("cocktailImageURL", cocktailImageURL)

        // 결과 페이지로 이동
        return "user/result" // result.html 템플릿 반환
    }

    @RequestMapping("/place")
    @Throws(Exception::class)
    fun getSuggestionPlace(@RequestParam("query") query: String, modelObj: Model): String {
        // Query 분석 및 추천 로직
        val answers = query.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val answer1 = answers[0]
        val answer2 = answers[1]
        var prompt = ""
        prompt += "suggest one cocktail which is $answer1 and $answer2. Send me with only JSON format(don't write ```json and depth is less than 2) which only contains name, description, ingredient, instruction, imageURL also each information is going to be json index and do not change the spelling(do not add 's' or something else)"
        val request = model?.let { ChatGPTRequest(it, prompt) }
        val chatGPTResponse = apiURL?.let { template!!.postForObject(it, request, ChatGPTResponse::class.java) }
        if (chatGPTResponse != null) {
            println(chatGPTResponse.choices?.get(0)?.message?.content)
        }
        val jsonParser = JSONParser()

        //3. To Object
        val obj = jsonParser.parse(chatGPTResponse?.choices?.get(0)?.message?.content ?: null)

        //4. To JsonObject
        val jsonObj = obj as JSONObject

        //print
        println(jsonObj["name"]) //sim
        println(jsonObj["description"])
        println(jsonObj["ingredient"])
        println(jsonObj["instruction"])
        val keyword = jsonObj["name"].toString()
        val cocktailImageURL = cocktailExternalApiImageURL!!.getCocktailImageURL(keyword)
        println(cocktailImageURL)

        // Model에 데이터 추가
        modelObj.addAttribute("cocktailName", jsonObj["name"])
        modelObj.addAttribute("cocktailDescription", jsonObj["description"])
        modelObj.addAttribute("cocktailIngredient", jsonObj["ingredient"])
        modelObj.addAttribute("cocktailInstruction", jsonObj["instruction"])
        modelObj.addAttribute("cocktailImageURL", cocktailImageURL)

        // 결과 페이지로 이동
        return "user/result" // result.html 템플릿 반환
    }
}