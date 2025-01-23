package org.programmers.cocktail.search.service

import org.modelmapper.ModelMapper
import org.modelmapper.PropertyMap
import org.programmers.cocktail.entity.CocktailLists
import org.programmers.cocktail.search.dto.CocktailListsTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CocktailListsMapper @Autowired constructor(private val modelMapper: ModelMapper) {
    init {
        // CocktailLists -> CocktailListsTO 변환 매핑
        modelMapper.addMappings<CocktailLists, CocktailListsTO>(object :
            PropertyMap<CocktailLists?, CocktailListsTO?>() {
            override fun configure() {
                map().setUserId(source.getUsers().getId())
                map().setCocktailId(source.getCocktails().getId())
            }
        })

        // CocktailListsTO -> CocktailLists 변환 매핑
        modelMapper.addMappings<CocktailListsTO, CocktailLists>(object :
            PropertyMap<CocktailListsTO?, CocktailLists?>() {
            override fun configure() {
                map().getUsers().setId(source.getUserId())
                map().getCocktails().setId(source.getCocktailId())
            }
        })
    }

    fun convertToCocktailsListsTO(cocktailLists: CocktailLists?): CocktailListsTO {
        return modelMapper.map(cocktailLists, CocktailListsTO::class.java)
    }

    fun convertToCocktailLists(cocktailListsTO: CocktailListsTO?): CocktailLists {
        return modelMapper.map(cocktailListsTO, CocktailLists::class.java)
    }
}
