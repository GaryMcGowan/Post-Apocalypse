package com.garymcgowan.postapocalypse.view.postdetails.mvp

import com.garymcgowan.postapocalypse.core.SchedulerProvider
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.network.NetworkRepository
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostDetailsPresenter @Inject constructor(
    private val network: NetworkRepository,
    private val schedulers: SchedulerProvider
) : PostDetailsContract.Presenter() {

    private lateinit var post: Post
    private lateinit var user: User

    override fun takePostAndUser(post: Post, user: User) {
        this.post = post
        this.user = user
        view?.displayPostDetails(post, user)
        fetchCommentsForPost(post)
    }

    override fun onRefreshComments() {
        fetchCommentsForPost(post)
    }

    private fun fetchCommentsForPost(post: Post) =
        network.getComments()
            //filter only comments related to this post
            .map { list -> list.filter { it.postId == post.id } }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSubscribe { view?.showCommentsLoading() }
            .doOnEvent { _, _ -> view?.hideCommentsLoading() }
            .subscribe(
                { view?.displayComments(it) }, // success
                { view?.displayErrorForComments() } //error
            )
            .also { disposables += it }
}