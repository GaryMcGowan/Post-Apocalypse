package com.garymcgowan.postapocalypse.view.postdetails.mvp

import com.garymcgowan.postapocalypse.core.SchedulerProvider
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.network.PostsApi
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostDetailsPresenter @Inject constructor(
    private val api: PostsApi,
    private val schedulers: SchedulerProvider
) : PostDetailsContract.Presenter() {

    private lateinit var post: Post

    override fun takePost(post: Post) {
        this.post = post
        fetchCommentsForPost(post)
    }

    override fun onRefreshComments() {
        fetchCommentsForPost(post)
    }

    private fun fetchCommentsForPost(post: Post) = api.fetchComments()
        .doOnSubscribe { view?.showCommentsLoading() }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .subscribe { list, error ->
            view?.hideCommentsLoading()
            list?.let { view?.displayComments(it.filter { it.postId == post.id }) }
            error?.let { view?.displayErrorForComments() }
        }
        .also { disposables += it }
}