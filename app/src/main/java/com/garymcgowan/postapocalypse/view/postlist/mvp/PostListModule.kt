package com.garymcgowan.postapocalypse.view.postlist.mvp

import dagger.Binds
import dagger.Module

@Module
abstract class PostListModule {

    @Binds
    abstract fun presenter(impl: PostListPresenter): PostListContract.Presenter

}