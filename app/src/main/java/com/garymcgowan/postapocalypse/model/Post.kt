package com.garymcgowan.postapocalypse.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(

    @Json(name = "id") val id: Int? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "body") val body: String? = null,
    @Json(name = "userId") val userId: Int? = null
) : Parcelable