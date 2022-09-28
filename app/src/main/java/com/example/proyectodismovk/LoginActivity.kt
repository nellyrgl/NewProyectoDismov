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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
//TODO:implementar el login automatico del usuario si ya tien cuenta y esta verificado

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        /*
        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        */

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val btnLogIn = findViewById<Button>(R.id.login)
        btnLogIn.setOnClickListener { authenticateUser() }

        val tvSwitchToRegister = findViewById<TextView>(R.id.text_register)
        tvSwitchToRegister.setOnClickListener { switchToRegister() }

        val tvregistroMaestros = findViewById<TextView>(R.id.registro_maestros)
        tvregistroMaestros.setOnClickListener{ registroMaestros() }

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
                    /*Toast.makeText(baseContext, "Inicio de sesion exitoso",
                        Toast.LENGTH_SHORT).show()*/

                    val user = FirebaseAuth.getInstance().currentUser
                    if(user!!.isEmailVerified){
                        Toast.makeText(baseContext, "Usuario Verificado",
                            Toast.LENGTH_SHORT).show()
                        showMainActivity()
                    }else{
                        Toast.makeText(baseContext, "Favor de verificar su correo",
                            Toast.LENGTH_SHORT).show()

                        user!!.sendEmailVerification()
                            .addOnSuccessListener {
                                /*Toast.makeText(baseContext, "Favor de verificar su correo",
                                    Toast.LENGTH_SHORT).show()

                                 */
                            }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Inicio de sesion fallido.",
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

    private fun registroMaestros(){
        val intent = Intent(this, RegisterActivityT::class.java)
        startActivity(intent)
        finish()
    }
}