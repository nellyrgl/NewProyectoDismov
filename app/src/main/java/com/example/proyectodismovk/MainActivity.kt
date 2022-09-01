package com.example.proyectodismovk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val btnChat = findViewById<Button>(R.id.chat)
        btnChat.setOnClickListener { chat() }

        val btnLogOut = findViewById<Button>(R.id.btnlogout)
        btnLogOut.setOnClickListener { logoutUser() }

    }

    private  fun chat(){
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, ListOfChatsActivity::class.java)
            intent.putExtra("user",currentUser.email)
            startActivity(intent)
            finish()
        }
    }

    private fun logoutUser() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}