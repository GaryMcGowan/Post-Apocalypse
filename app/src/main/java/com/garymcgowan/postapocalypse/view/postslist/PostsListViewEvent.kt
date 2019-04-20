package com.garymcgowan.postapocalypse.view.postslist

import com.garymcgowan.postapocalypse.model.Post

sealed class PostsListViewEvent {
    object InitPostsList : PostsListViewEvent()
    object RefreshPostsList : PostsListViewEvent()
    data class PostPressed(val past: Post) : PostsListViewEvent()
}