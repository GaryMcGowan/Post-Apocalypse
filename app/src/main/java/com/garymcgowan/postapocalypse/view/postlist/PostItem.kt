package com.garymcgowan.postapocalypse.view.postlist

import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.model.Post
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_post.*

class PostItem(private val post: Post) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.postTitle.text = post.title
    }

    override fun getLayout() = R.layout.item_post

    override fun isClickable() = true

    fun getPost() = post
}