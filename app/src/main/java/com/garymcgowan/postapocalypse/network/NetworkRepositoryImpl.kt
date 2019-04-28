package com.garymcgowan.postapocalypse.network

import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepositoryImpl @Inject constructor(
    private val api: PostsApi
) : NetworkRepository {
    override fun getPosts(): Single<List<Post>> = api.fetchPosts()

    override fun getUsers(): Single<List<User>> = api.fetchUsers()

    override fun getComments(): Single<List<Comment>> = api.fetchComments()
}