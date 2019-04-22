package com.garymcgowan.postapocalypse.mvi2.actions

import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.mvi2.base.MviAction

sealed class PostListAction : MviAction {
    object LoadAllPostsAction : PostListAction()
    //    object RefreshAllPostsAction : PostListAction()
    data class PostPressedAction(val post: Post) : PostListAction()
}