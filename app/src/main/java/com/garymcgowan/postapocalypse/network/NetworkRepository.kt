package com.garymcgowan.postapocalypse.network

import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import io.reactivex.Single

interface NetworkRepository {
    fun getPosts(): Single<List<Post>>
    fun getUsers(): Single<List<User>>
    fun getComments(): Single<List<Comment>>

}