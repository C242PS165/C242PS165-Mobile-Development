package com.tyas.smartfarm.model

data class ApiResponse(
    val candidates: List<ApiCandidate>
)

data class ApiCandidate(
    val content: ApiContent
)

data class ApiContent(
    val parts: List<ApiPart>
)

data class ApiPart(
    val text: String
)
