package com.garymcgowan.postapocalypse.mvi2.results

import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.mvi2.base.MviResult

sealed class PostListResult : MviResult {
    sealed class LoadAllPostsResult : PostListResult() {
        object Loading : LoadAllPostsResult()
        data class Success(val posts: List<Post>) : LoadAllPostsResult()
        data class Failure(val error: Throwable) : LoadAllPostsResult()
    }

    sealed class PostPressedResult : PostListResult() {
        data class Success(val post: Post) : PostPressedResult()
    }


}