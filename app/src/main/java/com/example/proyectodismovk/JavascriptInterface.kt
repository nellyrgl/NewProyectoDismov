package com.example.proyectodismovk

import android.webkit.JavascriptInterface
import android.widget.Toast

class JavascriptInterface(val callActivity: CallActivity) {

    @JavascriptInterface
    public fun onPeerConnected(){
        callActivity.onPeerConnected()
    }
}