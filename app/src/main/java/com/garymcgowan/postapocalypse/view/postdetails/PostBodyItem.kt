package com.garymcgowan.postapocalypse.view.postdetails

import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_post_body.*

class PostBodyItem(
    val post: Post,
    val user: User
) : Item(post.getId()) {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.userName.text = user.name
        viewHolder.postTitle.text = post.title
        viewHolder.postBody.text = post.body
    }

    override fun getLayout() = R.layout.item_post_body
    override fun isClickable() = false

    companion object {
        private fun Post.getId() = (id.toLong() + javaClass.hashCode() * 1000)
    }
}