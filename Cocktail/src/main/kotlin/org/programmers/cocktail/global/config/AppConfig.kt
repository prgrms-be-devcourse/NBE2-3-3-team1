package org.programmers.cocktail.global.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class AppConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun modelMapper() = ModelMapper().apply{
        configuration.isFieldMatchingEnabled = true
        configuration.fieldAccessLevel = org.modelmapper.config.Configuration.AccessLevel.PRIVATE
    }
}
