package com.garymcgowan.postapocalypse.view.postlist.mvp

import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.mvpbase.BaseContract
import com.garymcgowan.postapocalypse.view.postlist.PostItemViewState

interface PostListContract : BaseContract {

    interface View : BaseContract.View {
        fun showPostListLoading()
        fun hidePostListLoading()
        fun displayListViewState(state: List<PostItemViewState>)
        fun displayErrorForPostList()
        fun goToPost(post: Post, user: User)
    }

    abstract class Presenter : BaseContract.BasePresenter<View>() {
        abstract fun onListRefreshed()
        abstract fun onItemPressed(post: Post, user: User)
    }
}