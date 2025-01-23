package org.programmers.cocktail

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CocktailApplication

fun main(args: Array<String>) {
    runApplication<CocktailApplication>(*args)
}
