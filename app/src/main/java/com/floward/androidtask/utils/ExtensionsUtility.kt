package com.floward.androidtask.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun showToast(context: Context, messageToShow: String) {
    Toast.makeText(context, messageToShow, Toast.LENGTH_SHORT).show()
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
