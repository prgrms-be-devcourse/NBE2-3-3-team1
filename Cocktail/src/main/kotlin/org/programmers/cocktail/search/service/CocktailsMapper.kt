package org.programmers.cocktail.search.service

import org.modelmapper.ModelMapper
import org.programmers.cocktail.entity.Cocktails
import org.programmers.cocktail.search.dto.CocktailsTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class CocktailsMapper {
    @Autowired
    private val modelMapper: ModelMapper? = null

    fun convertToCocktailsTOList(cocktailsList: List<Cocktails?>): List<CocktailsTO> {
        val cocktailsTOList = cocktailsList.stream()
            .map { cocktails: Cocktails? ->
                modelMapper!!.map(
                    cocktails,
                    CocktailsTO::class.java
                )
            }
            .collect(Collectors.toList())
        return cocktailsTOList
    }

    fun convertToCocktailsList(cocktailsTOList: List<CocktailsTO?>): List<Cocktails> {
        val cocktailsList = cocktailsTOList.stream()
            .map { cocktailsTO: CocktailsTO? ->
                modelMapper!!.map(
                    cocktailsTO,
                    Cocktails::class.java
                )
            }
            .collect(Collectors.toList())
        return cocktailsList
    }

    fun convertToCocktails(cocktailsTO: CocktailsTO?): Cocktails {
        return modelMapper!!.map(cocktailsTO, Cocktails::class.java)
    }

    fun convertToCocktailsTO(cocktails: Cocktails?): CocktailsTO {
        return modelMapper!!.map(cocktails, CocktailsTO::class.java)
    }
}
