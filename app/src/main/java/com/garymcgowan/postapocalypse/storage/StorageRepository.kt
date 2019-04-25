package com.garymcgowan.postapocalypse.storage

import com.garymcgowan.postapocalypse.model.Post
import io.reactivex.Observable
import io.reactivex.Single

interface StorageRepository {
    fun getBookmarks(): Observable<List<Post>>
    fun setBookmarked(post: Post, bookmarked: Boolean): Single<Unit>
    fun isBookmarked(post: Post): Single<Boolean>
}