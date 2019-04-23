package com.garymcgowan.postapocalypse.network

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.garymcgowan.postapocalypse.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageLoaderImpl @Inject constructor(
    appContext: Context,
    @AvatarUrlString private val avatarUrlString: String
) : ImageLoader {
    private val glide = Glide.with(appContext)

    override fun loadImage(view: AppCompatImageView, url: String) {
        glide.load(url).into(view)
    }

    override fun loadAvatar(view: AppCompatImageView, email: String?) {
        glide.load("$avatarUrlString/100/$email.png")
            .placeholder(R.color.colorBackgroundSecondary)
            .error(R.color.colorBackgroundSecondary)
            .fallback(R.color.colorBackgroundSecondary)
            .circleCrop()
            .into(view)
    }

}