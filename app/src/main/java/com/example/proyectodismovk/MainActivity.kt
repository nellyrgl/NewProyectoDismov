package com.example.proyectodismovk

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    val requestcode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        loadLocate()
        val btnChat = findViewById<Button>(R.id.chat)
        btnChat.setOnClickListener { chat() }

        val btnLenguaje = findViewById<Button>(R.id.cambiar_lenguaje)
        btnLenguaje.setOnClickListener { cambiarlenguaje() }
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)

        val btnLogOut = findViewById<Button>(R.id.btnlogout)
        btnLogOut.setOnClickListener { logoutUser() }

        val btnVideo = findViewById<Button>(R.id.video)
        btnVideo.setOnClickListener { video() }

        if(!isPermissionGranted()){
            askPermissions()
        }
    }

    private fun video() {
        val intent = Intent(this, CallActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isPermissionGranted(): Boolean{
        permissions.forEach {
            if(ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    private fun askPermissions(){
        ActivityCompat.requestPermissions(this, permissions, requestcode)
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

    private fun cambiarlenguaje() {
        val listItems = arrayOf("Español", "English")

        val mBuilder = AlertDialog.Builder(this@MainActivity)
        mBuilder.setTitle(getString(R.string.lenguaje))
        mBuilder.setSingleChoiceItems(listItems, -1){ dialog, which ->
            if (which == 0){
                setLocate ("es")
                recreate()
            }
            else if (which == 1){
                setLocate ("en")
                recreate()
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun setLocate(Lang: String){
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang",Lang)
        editor.apply()
    }

    private fun loadLocate(){
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        setLocate(language.toString())
    }

    private fun logoutUser() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}