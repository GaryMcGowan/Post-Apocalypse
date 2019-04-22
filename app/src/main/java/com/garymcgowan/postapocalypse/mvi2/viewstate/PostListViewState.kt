package com.garymcgowan.postapocalypse.mvi2.viewstate

import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.mvi2.base.MviViewState

data class PostListViewState(
    val isLoading: Boolean,
    val posts: List<Post>,
    val error: Throwable? = null
) : MviViewState {

    companion object {
        fun idle(): PostListViewState = PostListViewState(
            false, emptyList(), null
        )
    }
}