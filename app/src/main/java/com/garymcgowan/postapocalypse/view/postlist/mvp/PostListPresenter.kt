package com.garymcgowan.postapocalypse.view.postlist.mvp

import com.garymcgowan.postapocalypse.core.SchedulerProvider
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.network.PostsApi
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostListPresenter @Inject constructor(
    private val api: PostsApi,
    private val schedulers: SchedulerProvider
) : PostListContract.Presenter() {

    override fun takeView(view: PostListContract.View) {
        super.takeView(view)
        fetchPosts()
    }

    override fun onListRefreshed() {
        fetchPosts()
    }

    override fun onItemPressed(post: Post) {
        view?.goToPost(post)
    }

    private fun fetchPosts() = api.fetchPosts()
        .doOnSubscribe { view?.showPostListLoading() }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .subscribe { list, error ->
            view?.hidePostListLoading()
            list?.let { view?.displayPostList(it) }
            error?.let { view?.displayErrorForPostList() }
        }
        .also { disposables += it }
}