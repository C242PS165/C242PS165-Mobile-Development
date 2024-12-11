package com.tyas.smartfarm.api

import com.tyas.smartfarm.model.ApiResponse
import com.tyas.smartfarm.model.ChatRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {

    @POST("v1beta/models/gemini-1.5-flash-latest:generateContent")
    suspend fun sendMessage(
        @Query("key") apiKey: String,
        @Body request: ChatRequest
    ): Response<ApiResponse>
}
