package com.garymcgowan.postapocalypse.storage

import com.garymcgowan.postapocalypse.model.Post
import io.reactivex.Observable

interface StorageRepository {
    fun getBookmarks(): Observable<List<Post>>
    fun addBookmark(post: Post)
    fun isBookmarked(post: Post): Observable<Boolean>
}