package org.programmers.cocktail.suggestion.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestTemplate

@Configuration
open class OpenAiConfig {
    @Value("\${openai.api.key}")
    private val openAiKey: String? = null

    @Bean
    open fun template(): RestTemplate {
        val restTemplate = RestTemplate()
        restTemplate.interceptors.add(ClientHttpRequestInterceptor { request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution ->
            request.headers.add("Authorization", "Bearer $openAiKey")
            execution.execute(request, body)
        })
        return restTemplate
    }
}
