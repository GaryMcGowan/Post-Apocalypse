package com.garymcgowan.postapocalypse.model

import com.squareup.moshi.Json

data class Post(

    @Json(name = "id") val id: Int? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "body") val body: String? = null,
    @Json(name = "userId") val userId: Int? = null
)

sealed class PostState {
    object Loading : PostState()
    data class Content(val posts: List<Post>) : PostState()
    data class Error(val throwable: Throwable) : PostState()
}