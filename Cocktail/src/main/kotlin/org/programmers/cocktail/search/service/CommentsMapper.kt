package org.programmers.cocktail.search.service

import org.modelmapper.ModelMapper
import org.modelmapper.PropertyMap
import org.programmers.cocktail.entity.Comments
import org.programmers.cocktail.search.dto.CommentsTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class CommentsMapper @Autowired constructor(private val modelMapper: ModelMapper) {
    init {
        // Comments -> CommentsTO 변환 매핑
        modelMapper.addMappings<Comments, CommentsTO>(object :
            PropertyMap<Comments?, CommentsTO?>() {
            override fun configure() {
                map().setUserId(source.getUsers().getId())
                map().setCocktailId(source.getCocktails().getId())
                map().setUserName(source.getUsers().getName())
            }
        })

        // CommentsTO -> Comments 변환 매핑
        modelMapper.addMappings<CommentsTO, Comments>(object :
            PropertyMap<CommentsTO?, Comments?>() {
            override fun configure() {
                map().getUsers().setId(source.getUserId())
                map().getCocktails().setId(source.getCocktailId())
            }
        })
    }

    fun convertToCommentsTOList(commentsList: List<Comments?>): List<CommentsTO> {
        val commentsTOList = commentsList.stream()
            .map { comments: Comments? ->
                modelMapper.map(
                    comments,
                    CommentsTO::class.java
                )
            }
            .collect(Collectors.toList())
        return commentsTOList
    }

    fun convertToComments(commentsTO: CommentsTO?): Comments {
        return modelMapper.map(commentsTO, Comments::class.java)
    }
}
