package com.example.proyectodismovk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListOfChatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_chats)

        val recycle = findViewById<RecyclerView>(R.id.recyclerView)
        recycle.layoutManager =LinearLayoutManager(this)
        recycle.adapter = NamesAdapter()

        val list = resources.getStringArray(R.array.names)

        (recycle.adapter as NamesAdapter).setListNames(list.asList())

        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        btnRegresar.setOnClickListener { showMainActivity() }

    }

    private fun showMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}