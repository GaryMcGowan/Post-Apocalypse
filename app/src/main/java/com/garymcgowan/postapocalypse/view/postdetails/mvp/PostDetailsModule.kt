package com.garymcgowan.postapocalypse.view.postdetails.mvp

import dagger.Binds
import dagger.Module

@Module
abstract class PostDetailsModule {

    @Binds
    abstract fun presenter(impl: PostDetailsPresenter): PostDetailsContract.Presenter

}