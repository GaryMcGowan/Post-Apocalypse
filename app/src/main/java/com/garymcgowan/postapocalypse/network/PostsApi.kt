package com.garymcgowan.postapocalypse.network

import io.reactivex.Single
import retrofit2.http.GET

interface PostsApi {

    @GET("/posts")
    fun fetchPosts(): Single<List<Post>>

    @GET("/users")
    fun fetchUsers(): Single<List<User>>

    @GET("/comments")
    fun fetchComments(): Single<List<Comment>>
}