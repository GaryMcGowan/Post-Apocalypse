package com.garymcgowan.postapocalypse.mvi2.intents

import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.mvi2.base.MviIntent

sealed class PostListIntent : MviIntent {
    object LoadAllPostsIntent : PostListIntent()
    object RefreshAllPostsIntent : PostListIntent()
    data class PostPressedIntent(val post: Post) : PostListIntent()
}