package com.garymcgowan.postapocalypse.view.base

import com.garymcgowan.postapocalypse.view.MainActivity
import com.garymcgowan.postapocalypse.view.bookmarks.BookmarksFragment
import com.garymcgowan.postapocalypse.view.postdetails.PostDetailsFragment
import com.garymcgowan.postapocalypse.view.postdetails.mvp.PostDetailsModule
import com.garymcgowan.postapocalypse.view.postlist.PostListFragment
import com.garymcgowan.postapocalypse.view.postlist.mvp.PostListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(
    includes = [
        PostListModule::class,
        PostDetailsModule::class
    ]
)
abstract class ViewModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindPostListFragment(): PostListFragment

    @ContributesAndroidInjector
    abstract fun bindBookmarksFragment(): BookmarksFragment

    @ContributesAndroidInjector
    abstract fun bindPostDetailsFragment(): PostDetailsFragment
}
