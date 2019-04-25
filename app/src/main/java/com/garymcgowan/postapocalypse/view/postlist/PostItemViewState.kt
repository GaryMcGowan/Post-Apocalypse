package com.garymcgowan.postapocalypse.view.postlist

import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User

data class PostItemViewState(
    val post: Post,
    val user: User,
    val comments: List<Comment>
)