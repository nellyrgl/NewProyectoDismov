package com.example.proyectodismovk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class CallActivity : AppCompatActivity() {
    var username = ""
    var friendsUsername = ""

    var isPeerConnected = false

    val db = Firebase.database
    val firebaseRef = db.getReference("users")

    var isAudio = true
    var isVideo = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

        intent.getStringExtra("user")?.let { username = it }
        firebaseRef.child("users").setValue(username)

        if(username.isNotEmpty()){
            Toast.makeText(baseContext, "El usuario es: "+username, Toast.LENGTH_SHORT).show()
        }

        val callBtn = findViewById<Button>(R.id.callBtn)
        callBtn.setOnClickListener {
            val friendNameEdit = findViewById<TextView>(R.id.friendNameEdit)
            friendsUsername = friendNameEdit.text.toString()
            sendCallRequest()
        }

        val toggleAudioBtn = findViewById<ImageView>(R.id.toggleAudioBtn)
        toggleAudioBtn.setOnClickListener {
            isAudio = !isAudio
            callJavascriptFunction("javascript:toggleAudio(\"${isAudio}\")")
            toggleAudioBtn.setImageResource(if (isAudio) R.drawable.ic_baseline_mic_24 else R.drawable.ic_baseline_mic_off_24 )
        }

        val toggleVideoBtn = findViewById<ImageView>(R.id.toggleVideoBtn)
        toggleVideoBtn.setOnClickListener {
            isVideo = !isVideo
            callJavascriptFunction("javascript:toggleVideo(\"${isVideo}\")")
            toggleVideoBtn.setImageResource(if (isVideo) R.drawable.ic_baseline_videocam_24 else R.drawable.ic_baseline_videocam_off_24 )
        }

        setupWebView()
    }

    private fun sendCallRequest() {
        if (!isPeerConnected) {
            Toast.makeText(this, "You're not connected. Check your internet", Toast.LENGTH_LONG).show()
            return
        }

        firebaseRef.child("users").child("incoming").setValue(username)
        firebaseRef.child("friendsUsername").child("isAvailable").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.value.toString() == "true") {
                    listenForConnId()
                }
            }
        })
    }

    private fun listenForConnId() {
        firebaseRef.child("friendsUsername").child("connId").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null)
                    return
                switchToControls()
                callJavascriptFunction("javascript:startCall(\"${snapshot.value}\")")
            }

        })
    }

    private fun setupWebView() {
        val webView = findViewById<WebView>(R.id.webView)
        webView.webChromeClient = object: WebChromeClient(){
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources)
            }
        }

        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.addJavascriptInterface(JavascriptInterface(this), "Android")

        loadVideoCall()
    }

    private fun loadVideoCall() {
        val webView = findViewById<WebView>(R.id.webView)
        val filePath = "file:android_asset/call.html"
        webView.loadUrl(filePath)

        webView.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                initializePeer()
            }
        }
    }

    var uniqueId = ""

    private fun initializePeer() {
        uniqueId = getUniqueID()
        callJavascriptFunction("javascript:init(\"${uniqueId}\")")

        firebaseRef.child("users").child("incoming").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                onCallRequest(snapshot.value as? String)
            }
        })
    }

    private fun onCallRequest(caller: String?) {
        if (caller == null) return

        val callLayout = findViewById<RelativeLayout>(R.id.callLayout)
        val incomingCallTxt = findViewById<TextView>(R.id.incomingCallTxt)
        callLayout.visibility = View.VISIBLE
        incomingCallTxt.text = "$caller is calling..."

        val acceptBtn = findViewById<ImageView>(R.id.acceptBtn)
        acceptBtn.setOnClickListener {
            firebaseRef.child("users").child("connId").setValue(uniqueId)
            firebaseRef.child("users").child("isAvailable").setValue(true)

            callLayout.visibility = View.GONE
            switchToControls()
        }

        val rejectBtn = findViewById<ImageView>(R.id.rejectBtn)
        rejectBtn.setOnClickListener {
            firebaseRef.child("users").child("incoming").setValue(null)
            callLayout.visibility = View.GONE
        }
    }

    private fun switchToControls() {
        val inputLayout = findViewById<RelativeLayout>(R.id.inputLayout)
        val callControlLayout = findViewById<LinearLayout>(R.id.callControlLayout)
        inputLayout.visibility = View.GONE
        callControlLayout.visibility = View.VISIBLE
    }


    private fun getUniqueID(): String {
        return UUID.randomUUID().toString()
    }

    private fun callJavascriptFunction(functionString: String) {
        val webView = findViewById<WebView>(R.id.webView)
        webView.post { webView.evaluateJavascript(functionString, null) }
    }


    fun onPeerConnected() {
        isPeerConnected = true
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        val webView = findViewById<WebView>(R.id.webView)
        firebaseRef.child("users").setValue(null)
        webView.loadUrl("about:blank")
        super.onDestroy()
    }
}