package com.floward.androidtask.utils

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.floward.androidtask.R


class ProgressDialog constructor(private val activity: Activity) {
    private var dialog:Dialog? = null

    fun showDialog() {
        dialog = Dialog(activity)
        dialog?.let {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            it.setCancelable(false)
            it.setContentView(R.layout.dialog_loading)
            val gifImageView = it.findViewById<ImageView>(R.id.custom_loading_imageView)
            Glide.with(activity)
                .asGif()
                .load(R.drawable.loading)
                .centerCrop()
                .into(gifImageView)
            it.show()
        }

    }

    fun hideDialog() {
        dialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }
}