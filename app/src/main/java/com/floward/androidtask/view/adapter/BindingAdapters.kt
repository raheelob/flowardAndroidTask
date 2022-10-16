package com.floward.androidtask.view.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
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

    @BindingAdapter("imagePathCircleCrop")
    @JvmStatic
    fun imagePathCircleCrop(appCompatImageView: AppCompatImageView, url: String?) {
        url?.let {
            Glide
                .with(appCompatImageView.context)
                .load(it)
                .circleCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(appCompatImageView)
        }
    }

    @BindingAdapter("setUserCount")
    @JvmStatic
    fun setUserCount(appCompatTextView: AppCompatTextView, count: Int?) {
        count?.let {
            appCompatTextView.text = buildString {
                append("You have ")
                append(count)
                append(" posts")
            }
        }
    }


}