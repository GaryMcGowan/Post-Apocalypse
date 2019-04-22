package com.garymcgowan.postapocalypse.mvi2.viewstate

import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.mvi2.base.MviViewState

data class PostDetailsViewState(
    val isLoadingComments: Boolean,
    val isLoadingUser: Boolean,
    val post: Post?,
    val user: User?,
    val comments: List<Comment>,
    val errorComments: Throwable? = null,
    val errorUser: Throwable? = null
) : MviViewState {

    companion object {
        fun idle(): PostDetailsViewState = PostDetailsViewState(
            false,
            false,
            null,
            null,
            emptyList(),
            null,
            null
        )
    }
}
