package com.floward.androidtask.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment

fun showToast(context: Context, messageToShow: String) {
    Toast.makeText(context, messageToShow, Toast.LENGTH_SHORT).show()
}

@SuppressLint("SetJavaScriptEnabled")
fun initWebView(webView: WebView, webUrl : String?) {
    webView.apply {
        webViewClient = WebViewClient()
        webUrl?.let { loadUrl(it) }
        settings.javaScriptEnabled = true
        settings.setSupportZoom(true)
    }
}

fun Activity.showDialog(mDialog: ProgressDialog){
    mDialog.showDialog()
}

fun Activity.hideLoading(mDialog: ProgressDialog){
    mDialog.hideDialog()
}

fun Fragment.showDialog(mDialog: ProgressDialog){
    mDialog.showDialog()
}

fun Fragment.hideLoading(mDialog: ProgressDialog){
    mDialog.hideDialog()
}
