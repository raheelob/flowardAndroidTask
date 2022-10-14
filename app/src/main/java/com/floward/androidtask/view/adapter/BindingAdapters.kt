package com.floward.androidtask.view.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.floward.androidtask.R

object BindingAdapters {
    @BindingAdapter("imagePath")
    @JvmStatic
    fun imagePath(appCompatImageView: AppCompatImageView, url: String?) {
        url?.let {
            Glide
                .with(appCompatImageView.context)
                .load(it)
                .placeholder(R.drawable.placeholder_image)
                .into(appCompatImageView)
        }
    }
}