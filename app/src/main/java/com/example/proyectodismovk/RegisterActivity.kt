package com.example.proyectodismovk

import android.app.Person
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    // creacion de usuario con funcion de firestore
    private val personCollectionRef = Firebase.firestore.collection("usuarios")

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

        val etMatricula = findViewById<EditText>(R.id.matricula)
        val matricula = etMatricula.text.toString().toInt()

        val etPassword = findViewById<EditText>(R.id.register_password)
        val password = etPassword.text.toString().trim()

        val etConfirmPassword = findViewById<EditText>(R.id.register_password_confirm)
        val confirmPassword = etConfirmPassword.text.toString().trim()

        val usuario = Usuario(email, password, matricula)



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
            etConfirmPassword.error = "Las contraseñas NO coinciden!"
            etConfirmPassword.requestFocus()
        }
        else{
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        switchToLogIn()
                        guardarUsuario(usuario)
                        val user = FirebaseAuth.getInstance().currentUser
                        user!!.sendEmailVerification()
                            .addOnSuccessListener {
                                switchToLogIn()
                                Toast.makeText(baseContext, "Correo de verificacion enviado",
                                    Toast.LENGTH_SHORT).show()
                            }

                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(baseContext, "Usuario creado correctamente.",
                            Toast.LENGTH_SHORT).show()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Creacion de usuario fallido.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    private fun guardarUsuario(usuario: Usuario){
        personCollectionRef.add(usuario)
    }
    private fun switchToLogIn() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}