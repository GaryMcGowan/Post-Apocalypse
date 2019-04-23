package com.garymcgowan.postapocalypse.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    @Json(name = "id") val id: Int,
    @Json(name = "userId") val userId: Int,
    @Json(name = "title") val title: String? = null,
    @Json(name = "body") val body: String? = null,
    val bookmarked: Boolean = false
) : Parcelable