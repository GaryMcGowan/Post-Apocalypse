package com.garymcgowan.postapocalypse.view.postdetails

import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.network.ImageLoader
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_comment.*

class CommentItem(
    val comment: Comment,
    private val imageLoader: ImageLoader? = null
) : Item(comment.getId()) {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.bodyText.text = comment.body
        viewHolder.userName.text = comment.name

        imageLoader?.loadAvatar(viewHolder.userImageView, comment.email)
    }

    override fun getLayout() = R.layout.item_comment
    override fun isClickable() = false

    companion object {
        private fun Comment.getId() = (id.toLong() + javaClass.hashCode() * 1000)
    }
}