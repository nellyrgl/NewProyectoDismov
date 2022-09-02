package com.example.proyectodismovk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ListOfChatsActivity : AppCompatActivity() {
    private var user =""
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_chats)

        intent.getStringExtra("user")?.let { user = it }

        if(user.isNotEmpty()){
            initViews()
        }

        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        btnRegresar.setOnClickListener { showMainActivity() }
    }

    private fun initViews(){
        val btnBuscar = findViewById<Button>(R.id.newChatButton)
        btnBuscar.setOnClickListener { newChat() }

        val recycle = findViewById<RecyclerView>(R.id.listChatsRecyclerView)
        recycle.layoutManager =LinearLayoutManager(this)
        recycle.adapter =
            ChatAdapter{ chat ->
                chatSelected(chat)
        }

        val userRef = db.collection("users").document(user)

        userRef.collection("chats")
            .get()
            .addOnSuccessListener { chats ->
                val listChats = chats.toObjects(Chat::class.java)
                (recycle.adapter as ChatAdapter).setData(listChats)
            }
    }

    private fun chatSelected(chat: Chat){
        val intent = Intent(this, ChatActivity::class.java )
        intent.putExtra("chatID",chat.id)
        intent.putExtra("user",user)
        startActivity(intent)
    }

    private fun newChat(){
        val chatID = UUID.randomUUID().toString()
        val otherUser = findViewById<EditText>(R.id.newChatText).text.toString()
        val users = listOf(user, otherUser)

        val chat = Chat(
            id = chatID,
            name = "Chat con $otherUser",
            users = users
        )

        db.collection("chats").document(chatID).set(chat)
        db.collection("users").document(user).collection("chats").document(chatID).set(chat)
        db.collection("users").document(otherUser).collection("chats").document(chatID).set(chat)

        val intent = Intent(this, ChatActivity::class.java )
        intent.putExtra("chatID", chatID)
        intent.putExtra("user",user)
        startActivity(intent)
    }

    private fun showMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}