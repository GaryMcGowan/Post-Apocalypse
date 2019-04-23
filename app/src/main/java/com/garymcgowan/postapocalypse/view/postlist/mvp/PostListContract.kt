package com.garymcgowan.postapocalypse.view.postlist.mvp

import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.mvpbase.BaseContract

interface PostListContract : BaseContract {

    interface View : BaseContract.View {
        fun showPostListLoading()
        fun hidePostListLoading()
        fun displayPostList(posts: List<Triple<Post, User, List<Comment>>>)
        fun displayErrorForPostList()
        fun goToPost(post: Post, user: User)
    }

    abstract class Presenter : BaseContract.BasePresenter<View>() {
        abstract fun onListRefreshed()
        abstract fun onItemPressed(post: Post, user: User)
    }
}