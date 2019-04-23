package com.garymcgowan.postapocalypse.view.postlist.mvp

import com.garymcgowan.postapocalypse.core.SchedulerProvider
import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.network.PostsApi
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@Singleton
class PostListPresenter @Inject constructor(
    private val api: PostsApi,
    private val schedulers: SchedulerProvider
) : PostListContract.Presenter() {

    // ViewState stores Posts by User with Comments[] and keeps in memory (while presenter lives)
    private var viewState: List<Triple<Post, User, List<Comment>>> by Delegates.observable(emptyList()) { _, old, new ->
        if (old != new) publish()
    }

    override fun publish() {
        view?.displayPostList(viewState)
    }

    override fun takeView(view: PostListContract.View) {
        super.takeView(view)
        fetchPostsAndUser()
    }

    override fun onListRefreshed() {
        fetchPostsAndUser()
    }

    override fun onItemPressed(post: Post, user: User) {
        view?.goToPost(post, user)
    }

    private fun fetchPostsAndUser() =
        Singles.zip(
            api.fetchPosts(),
            api.fetchUsers(),
            api.fetchComments()
        ) { posts, users, comments ->

            //combine all api calls into Posts by User with Comments[]
            posts.mapNotNull { post ->
                users.find { user -> user.id == post.userId }?.let { user ->
                    Triple(post, user, comments.filter { it.postId == post.id })
                }
            }
        }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSubscribe { view?.showPostListLoading() }
            .doOnEvent { _, _ -> view?.hidePostListLoading() }
            .subscribe(
                { viewState = it }, // success
                { view?.displayErrorForPostList() } //error
            )
            .also { disposables += it }
}