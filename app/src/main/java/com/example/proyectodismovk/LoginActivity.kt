package com.example.proyectodismovk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val btnLogIn = findViewById<Button>(R.id.login)
        btnLogIn.setOnClickListener { authenticateUser() }

        val tvSwitchToRegister = findViewById<TextView>(R.id.text_register)
        tvSwitchToRegister.setOnClickListener { switchToRegister() }

    }

    private fun authenticateUser(){
        val etEmail = findViewById<EditText>(R.id.login_email)
        val email = etEmail.text.toString().trim()

        val etPassword = findViewById<EditText>(R.id.login_password)
        val password = etPassword.text.toString().trim()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(baseContext, "signInWithEmail:SUCCESS",
                        Toast.LENGTH_SHORT).show()

                    val user = FirebaseAuth.getInstance().currentUser
                    if(user!!.isEmailVerified){
                        Toast.makeText(baseContext, "User Verified",
                            Toast.LENGTH_SHORT).show()
                        showMainActivity()
                    }else{
                        user!!.sendEmailVerification()
                            .addOnSuccessListener {
                                Toast.makeText(baseContext, "Verify Email",
                                    Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun switchToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}