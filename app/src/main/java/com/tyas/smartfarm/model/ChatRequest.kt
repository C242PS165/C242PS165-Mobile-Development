package com.tyas.smartfarm.model

data class ChatRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)
