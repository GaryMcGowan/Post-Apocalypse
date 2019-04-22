package com.garymcgowan.postapocalypse.storage

import com.garymcgowan.postapocalypse.model.Post
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SimpleStorageImpl @Inject constructor() : StorageRepository {

    private val memoryStorage = Hashtable<Int, Post>()

    override fun getBookmarks(): Observable<List<Post>> =
        Observable.just(memoryStorage.values.toList())

    override fun addBookmark(post: Post) {
        memoryStorage.put(post.id, post)
    }

}