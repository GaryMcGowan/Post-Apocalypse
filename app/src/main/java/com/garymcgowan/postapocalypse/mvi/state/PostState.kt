package com.garymcgowan.postapocalypse.mvi.state

import com.garymcgowan.postapocalypse.model.Post

sealed class PostState {
    object Loading : PostState()
    data class Content(val posts: List<Post>) : PostState()
    data class Error(val throwable: Throwable) : PostState()
    data class PostPressed(val post: Post) : PostState()
}