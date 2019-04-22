package com.garymcgowan.postapocalypse.mvi.intent

import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.mvi.PostsModelStore
import com.garymcgowan.postapocalypse.mvi.state.PostState
import com.garymcgowan.postapocalypse.mvi.viewevent.PostsListViewEvent
import com.garymcgowan.postapocalypse.mvi.viewevent.PostsListViewEvent.*
import com.garymcgowan.postapocalypse.network.PostsApi
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsListIntentFactory @Inject constructor(private val postsModelStore: PostsModelStore, private val postsApi: PostsApi) {

    fun process(viewEvent: PostsListViewEvent) {
        postsModelStore.process(toIntent(viewEvent))
    }

    private fun toIntent(viewEvent: PostsListViewEvent): Intent<PostState> = when (viewEvent) {
        RefreshPostsList, InitPostsList -> buildReloadTasksIntention()
        is PostPressed -> intent {
            PostState.PostPressed(
                viewEvent.post
            )
        }
    }

    private fun buildReloadTasksIntention(): Intent<PostState> {

        return intent {
            fun success(postsList: List<Post>) = postsModelStore.process(intent {
                PostState.Content(postsList)
            })

            fun error(throwable: Throwable) = postsModelStore.process(intent {
                PostState.Error(throwable)
            })

            postsApi.fetchPosts().subscribeOn(Schedulers.io()).subscribe(::success, ::error)

            PostState.Loading
        }
    }
}