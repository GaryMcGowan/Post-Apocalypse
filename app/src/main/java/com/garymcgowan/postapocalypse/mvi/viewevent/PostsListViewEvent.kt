package com.garymcgowan.postapocalypse.mvi.viewevent

import com.garymcgowan.postapocalypse.model.Post

sealed class PostsListViewEvent {
    object InitPostsList : PostsListViewEvent()
    object RefreshPostsList : PostsListViewEvent()
    data class PostPressed(val post: Post) : PostsListViewEvent()
}