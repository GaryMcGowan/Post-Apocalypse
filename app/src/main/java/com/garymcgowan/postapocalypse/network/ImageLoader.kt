package com.garymcgowan.postapocalypse.network

import androidx.appcompat.widget.AppCompatImageView

interface ImageLoader {
    fun loadImage(view: AppCompatImageView, url: String)

    fun loadAvatar(view: AppCompatImageView, email: String?)
}