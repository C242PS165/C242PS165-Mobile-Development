package com.tyas.smartfarm.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tyas.smartfarm.R

data class Message(val text: String, val isUser: Boolean)

class ChatAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) VIEW_TYPE_USER else VIEW_TYPE_BOT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = if (viewType == VIEW_TYPE_USER) {
            R.layout.item_message_user
        } else {
            R.layout.item_message_bot
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        (holder as ChatViewHolder).bind(message.text)
    }

    override fun getItemCount(): Int = messages.size

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.messageText)

        fun bind(message: String) {
            messageText.text = message
        }
    }
}
