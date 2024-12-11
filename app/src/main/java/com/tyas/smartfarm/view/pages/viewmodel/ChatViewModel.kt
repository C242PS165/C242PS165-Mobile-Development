package com.tyas.smartfarm.view.pages.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyas.smartfarm.api.ApiClient
import com.tyas.smartfarm.model.ChatRequest
import com.tyas.smartfarm.model.Content
import com.tyas.smartfarm.model.Part
import com.tyas.smartfarm.view.adapter.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {
    private val _messages = MutableLiveData<MutableList<Message>>().apply { value = mutableListOf() }
    val messages: LiveData<MutableList<Message>> get() = _messages

    private val apiKey = "AIzaSyDAtLlD6ukR705BrK0V7HCBV45h0RHlxN4"

    fun addMessage(message: Message) {
        _messages.value?.add(message)
        _messages.value = _messages.value
    }

    fun sendMessageToBot(userMessage: String) {
        // Tambahkan pesan user
        addMessage(Message(userMessage, isUser = true))

        // Tambahkan placeholder untuk loading
        addMessage(Message("Loading...", isUser = false))

        // Kirim pesan ke API Google Gemini
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    val request = ChatRequest(
                        contents = listOf(Content(parts = listOf(Part(text = userMessage))))
                    )
                    ApiClient.geminiApiService.sendMessage(apiKey, request)
                }

                // Hapus placeholder loading
                _messages.value?.let {
                    if (it.isNotEmpty()) {
                        it.removeAt(it.size - 1)
                        _messages.value = it
                    }
                }

                if (response.isSuccessful) {
                    val botResponse =
                        response.body()?.candidates?.get(0)?.content?.parts?.get(0)?.text ?: "Tidak ada jawaban."
                    // Tambahkan respons bot
                    addMessage(Message(botResponse, isUser = false))
                } else {
                    // Tambahkan pesan error
                    addMessage(Message("Bot: Gagal mendapatkan jawaban dari server.", isUser = false))
                }
            } catch (e: Exception) {
                // Hapus placeholder loading dan tambahkan pesan error
                _messages.value?.let {
                    if (it.isNotEmpty()) {
                        it.removeAt(it.size - 1)
                        _messages.value = it
                    }
                }
                addMessage(Message("Bot: Terjadi kesalahan: ${e.message}", isUser = false))
            }
        }
    }
}
