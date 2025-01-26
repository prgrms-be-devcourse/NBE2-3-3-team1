package org.programmers.cocktail.elasticsearch.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import java.time.LocalDateTime

@Data
@Builder
@AllArgsConstructor
data class ElasticsearchRequest(var timestamp: LocalDateTime) {
    var userId: Long? = null
    var age = 0
    var gender: String? = null

    var cocktailId: Long? = null
    var cocktailName: String? = null
    var category: String? = null
    var ingredients: String? = null

    var interactionType: String? = null
    var commentContent: String? = null
}

