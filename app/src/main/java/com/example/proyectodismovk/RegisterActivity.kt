package com.example.proyectodismovk

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val btnRegister = findViewById<Button>(R.id.register)
        btnRegister.setOnClickListener { registerUser() }

        val tvSwitchToLogIn = findViewById<TextView>(R.id.text_login)
        tvSwitchToLogIn.setOnClickListener { switchToLogIn() }
    }

    private fun registerUser(){
        val etEmail = findViewById<EditText>(R.id.register_email)
        val email = etEmail.text.toString().trim()

        val etPassword = findViewById<EditText>(R.id.register_password)
        val password = etPassword.text.toString().trim()

        val etConfirmPassword = findViewById<EditText>(R.id.register_password_confirm)
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "Correo Requerido!"
            etEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Ingrese un correo valido!"
            etEmail.requestFocus()
            return
        }

        if (password.length < 6) {
            etPassword.error = "Ingrese al menos 6 caracteres!"
            etPassword.requestFocus()
            return
        }

        if (password != confirmPassword) {
            etConfirmPassword.error = "La contraseña NO coincide!"
            etConfirmPassword.requestFocus()
        }
        else{
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(baseContext, "createUserWithEmail: SUCCESS.",
                            Toast.LENGTH_SHORT).show()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun switchToLogIn() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}