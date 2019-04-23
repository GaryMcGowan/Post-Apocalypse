package com.garymcgowan.postapocalypse.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment(
    @Json(name = "id") val id: Int,
    @Json(name = "postId") val postId: Int,
    @Json(name = "name") val name: String? = null,
    @Json(name = "body") val body: String? = null,
    @Json(name = "email") val email: String? = null
) : Parcelable