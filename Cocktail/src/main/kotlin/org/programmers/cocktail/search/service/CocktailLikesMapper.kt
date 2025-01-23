package org.programmers.cocktail.search.service

import org.modelmapper.ModelMapper
import org.modelmapper.PropertyMap
import org.programmers.cocktail.entity.CocktailLikes
import org.programmers.cocktail.search.dto.CocktailLikesTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CocktailLikesMapper @Autowired constructor(private val modelMapper: ModelMapper) {
    init {
        // CocktailLikes -> CocktailLikesTO 변환 매핑
        modelMapper.addMappings<CocktailLikes, CocktailLikesTO>(object :
            PropertyMap<CocktailLikes?, CocktailLikesTO?>() {
            override fun configure() {
                map(source?.users?.id, destination?.userId)
                map(source?.cocktails?.id, destination?.cocktailId)
            }
        })

        // CocktailLikesTO -> CocktailLikes 변환 매핑
        modelMapper.addMappings<CocktailLikesTO, CocktailLikes>(object :
            PropertyMap<CocktailLikesTO?, CocktailLikes?>() {
            override fun configure() {
                map(source?.userId, destination?.users?.id)
                map(source?.cocktailId, destination?.cocktails?.id)
            }
        })
    }

    fun convertToCocktailsLikesTO(cocktailLikes: CocktailLikes?): CocktailLikesTO {
        return modelMapper.map(cocktailLikes, CocktailLikesTO::class.java)
    }

    fun convertToCocktailLikes(cocktailLikesTO: CocktailLikesTO?): CocktailLikes {
        return modelMapper.map(cocktailLikesTO, CocktailLikes::class.java)
    }
}
