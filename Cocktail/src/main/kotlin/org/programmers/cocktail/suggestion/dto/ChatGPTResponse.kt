package org.programmers.cocktail.suggestion.dto

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
class ChatGPTResponse {
    var choices: List<Choice>? = null

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Choice {
        var index = 0
        var message: Message? = null
    }
}
