package com.garymcgowan.postapocalypse.view.postlist.mvp

import com.garymcgowan.postapocalypse.core.SchedulerProvider
import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.network.PostsApi
import com.garymcgowan.postapocalypse.storage.StorageRepository
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@Singleton
class PostListPresenter @Inject constructor(
    private val api: PostsApi,
    private val storage: StorageRepository,
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
        fetchPostsWithUserAndComments()
    }

    override fun onListRefreshed() {
        fetchPostsWithUserAndComments()
    }

    override fun onItemPressed(post: Post, user: User) {
        view?.goToPost(post, user)
    }

    override fun onBookmarkPressed(post: Post, bookmarked: Boolean) {
        val newPost = post.copy(bookmarked = bookmarked)
        disposables += storage.setBookmarked(newPost, bookmarked)
            .map {
                viewState.toMutableList().apply {
                    replaceAll {
                        if (it.first.id == post.id) it.copy(first = newPost) else it
                    }
                }.toList()
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(
                { viewState = it },
                { /*TODO error*/ throw it })
    }

    private fun fetchPostsWithUserAndComments() =
        Singles.zip(
            fetchPostsWithStorageState(),
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

    private fun fetchPostsWithStorageState() =
        api.fetchPosts()
            .flattenAsFlowable { it }
            .flatMapSingle { post -> storage.isBookmarked(post).map { post.copy(bookmarked = it) } }
            .collectInto(mutableListOf<Post>()) { list, post -> list += post }.map { it.toList() }

}