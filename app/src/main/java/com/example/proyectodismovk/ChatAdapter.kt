package com.example.proyectodismovk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(val chatClick: (Chat) -> Unit):RecyclerView.Adapter<ChatAdapter.ChatViewHolder>(){
    var chats: List<Chat> = emptyList()

    fun setData(list: List<Chat>){
        chats = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_chat,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.idText).text = chats[position].name
        holder.itemView.findViewById<TextView>(R.id.nameText).text = chats[position].name
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    class ChatViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
}