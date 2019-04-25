package com.garymcgowan.postapocalypse.view.postlist

import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.network.ImageLoader
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_post.*

class PostItem(
    val postItemViewState: PostItemViewState,
    private val imageLoader: ImageLoader? = null
) : Item(postItemViewState.post.getId()) {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.userName.text = postItemViewState.user.name
        viewHolder.postTitle.text = postItemViewState.post.title

        viewHolder.commentCount.apply {
            text = resources
                .getQuantityString(
                    R.plurals.number_comments,
                    postItemViewState.comments.size,
                    postItemViewState.comments.size
                )
        }

        imageLoader?.loadAvatar(viewHolder.userImageView, postItemViewState.user.email)
    }

    override fun getLayout() = R.layout.item_post
    override fun isClickable() = true

    companion object {
        private fun Post.getId() = (id.toLong() + javaClass.hashCode() * 1000)
    }
}