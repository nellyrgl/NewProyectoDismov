package com.example.proyectodismovk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NamesAdapter: RecyclerView.Adapter<NamesAdapter.NamesViewHolder>() {

    class NamesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    fun setListNames(list:List<String>){
        listOfNames =list
    }

    var listOfNames: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamesViewHolder {
        return NamesViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat,parent,false))
    }

    override fun onBindViewHolder(holder: NamesViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.idText).text = position.toString()
        holder.itemView.findViewById<TextView>(R.id.nameText).text = listOfNames[position]
    }

    override fun getItemCount(): Int {
        return listOfNames.size
    }

}