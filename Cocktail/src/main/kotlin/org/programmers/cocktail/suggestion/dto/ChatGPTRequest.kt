package org.programmers.cocktail.suggestion.dto

data class ChatGPTRequest(
    val model: String?,
    val messages: MutableList<Message> = mutableListOf()
) {
    constructor(model: String?, prompt: String) : this(
        model = model,
        messages = mutableListOf(Message("user", prompt))
    )
}
