package com.garymcgowan.postapocalypse.storage

import com.garymcgowan.postapocalypse.model.Post
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SimpleStorageImpl @Inject constructor() : StorageRepository {

    private val memoryStorage = Hashtable<Int, Post>()

    override fun getBookmarks(): Observable<List<Post>> =
        Observable.just(memoryStorage.values.toList())

    override fun setBookmarked(post: Post, bookmarked: Boolean): Single<Unit> =
        Single.create<Unit> {
            if (bookmarked) memoryStorage[post.id] = post
            else memoryStorage.remove(post.id)
            it.onSuccess(Unit)
    }

    override fun isBookmarked(post: Post): Single<Boolean> =
        Single.just(memoryStorage.containsKey(post.id))

}