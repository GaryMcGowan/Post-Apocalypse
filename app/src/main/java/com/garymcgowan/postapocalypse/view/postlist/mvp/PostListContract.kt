package com.garymcgowan.postapocalypse.view.postlist.mvp

import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.mvpbase.BaseContract

interface PostListContract : BaseContract {

    interface View : BaseContract.View {
        fun showPostListLoading()
        fun hidePostListLoading()
        fun displayPostList(posts: List<Post>)
        fun displayErrorForPostList()
        fun goToPost(post: Post)
    }

    abstract class Presenter : BaseContract.BasePresenter<View>() {
        abstract fun onListRefreshed()
        abstract fun onItemPressed(post: Post)
    }
}