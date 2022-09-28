package com.example.proyectodismovk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivityT : AppCompatActivity() {

    private val personCollectionRefT = Firebase.firestore.collection("maestros")
    private val personCollectionRef = Firebase.firestore.collection("usuarios")

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_t)

        auth = Firebase.auth

        val btnRegister = findViewById<Button>(R.id.registerT)
        btnRegister.setOnClickListener { registerTeacher() }

        val tvSwitchToLogin = findViewById<TextView>(R.id.text_loginT)
        tvSwitchToLogin.setOnClickListener { switchToLogIn() }

        val spinner = findViewById<Spinner>(R.id.spRoles)
        // val lista = listOf("maestro", "alumno")
        val lista = resources.getStringArray(R.array.roles)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        spinner.adapter = adaptador
    }

    private fun registerTeacher(){
        val etEmail = findViewById<EditText>(R.id.register_emailT)
        val email = etEmail.text.toString().trim()

        val etPassword = findViewById<EditText>(R.id.register_passwordT)
        val password = etPassword.text.toString().trim()

        val etConfirmPassword = findViewById<EditText>(R.id.register_password_confirmT)
        val confirmPassword = etConfirmPassword.text.toString().trim()

        val etMatricula = findViewById<EditText>(R.id.matriculaT)
        val matricula = etMatricula.text.toString().toInt()

        val spRoles = findViewById<Spinner>(R.id.spRoles)
        val roles = spRoles.selectedItemPosition.toString().trim()

        val maestro = Maestro(email, password)
        val usuario = Usuario(email, password, matricula, roles)

        if (email.isEmpty()){
            etEmail.error = "Correo Requerido!"
            etEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.error = "Ingrese un correo valido!"
            return
        }
        if (password.length < 6 ){
            etPassword.error = "Ingrese al menos 6 caracteres"
            return
        }
        if (password != confirmPassword) {
            etConfirmPassword.error = "Las contraseñas NO coinciden"
        }else{
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        guardarMaestro(maestro)
                        guardarUsuario(usuario)
                        val user = FirebaseAuth.getInstance().currentUser
                        user!!.sendEmailVerification()
                            .addOnSuccessListener {
                                switchToLogIn()
                                Toast.makeText(baseContext, "correo de Verificacion enviado", Toast.LENGTH_SHORT).show()
                            }
                        
                        Toast.makeText(baseContext, "Usuario Creado Correctamente", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(baseContext, "Creacion de Usuario Fallido", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun guardarUsuario(usuario: Usuario){
        personCollectionRef.add(usuario)
    }

    private fun guardarMaestro(maestro: Maestro){
        personCollectionRefT.add(maestro)
    }

    private fun switchToLogIn(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}