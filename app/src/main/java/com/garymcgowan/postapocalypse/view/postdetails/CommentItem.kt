package com.garymcgowan.postapocalypse.view.postdetails

import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.model.Comment
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_comment.*

class CommentItem(
    val comment: Comment
) : Item(comment.getId()) {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.postTitle.text = comment.body
    }

    override fun getLayout() = R.layout.item_comment
    override fun isClickable() = false

    companion object {
        private fun Comment.getId() = (id.toLong() + javaClass.hashCode() * 1000)
    }
}