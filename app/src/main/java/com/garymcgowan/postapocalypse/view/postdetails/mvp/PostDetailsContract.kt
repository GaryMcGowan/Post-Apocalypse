package com.garymcgowan.postapocalypse.view.postdetails.mvp

import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.mvpbase.BaseContract

interface PostDetailsContract : BaseContract {

    interface View : BaseContract.View {
        fun showCommentsLoading()
        fun hideCommentsLoading()
        fun displayPostDetails(post: Post, user: User)
        fun displayComments(comments: List<Comment>)
        fun displayErrorForComments()
    }

    abstract class Presenter : BaseContract.BasePresenter<View>() {
        abstract fun takePostAndUser(post: Post, user: User)
        abstract fun onRefreshComments()
    }
}