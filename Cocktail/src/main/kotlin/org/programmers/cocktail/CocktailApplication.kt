package org.programmers.cocktail

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class CocktailApplication

fun main(args: Array<String>) {
    runApplication<CocktailApplication>(*args)
}
