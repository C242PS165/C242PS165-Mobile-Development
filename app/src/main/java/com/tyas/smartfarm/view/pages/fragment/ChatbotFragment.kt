package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tyas.smartfarm.R
import com.tyas.smartfarm.view.adapter.ChatAdapter
import com.tyas.smartfarm.view.adapter.Message
import com.tyas.smartfarm.view.pages.viewmodel.ChatViewModel

class ChatbotFragment : Fragment() {

    private lateinit var chatViewModel: ChatViewModel
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatbot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tombol Kembali
        val buttonBack = view.findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Inisialisasi ViewModel
        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        // Inisialisasi RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewChat)
        val textViewEmpty = view.findViewById<TextView>(R.id.textViewEmpty)
        chatAdapter = ChatAdapter(messages)
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observasi pesan dari ViewModel
        chatViewModel.messages.observe(viewLifecycleOwner) { updatedMessages ->
            messages.clear()
            messages.addAll(updatedMessages)

            // Tampilkan/hilangkan teks kosong berdasarkan jumlah pesan
            textViewEmpty.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE

            chatAdapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(messages.size - 1) // Scroll ke pesan terbaru
        }

        // Setup input dan tombol kirim
        val editTextMessage = view.findViewById<EditText>(R.id.editTextMessage)
        val buttonSend = view.findViewById<ImageButton>(R.id.buttonSend)

        buttonSend.setOnClickListener {
            val userMessage = editTextMessage.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                editTextMessage.text.clear()
                chatViewModel.sendMessageToBot(userMessage)
            }
        }
    }
}
