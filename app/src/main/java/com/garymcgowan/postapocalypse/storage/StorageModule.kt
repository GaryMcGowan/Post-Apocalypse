package com.garymcgowan.postapocalypse.storage

import dagger.Binds
import dagger.Module

@Module
abstract class StorageModule {

    @Binds
    abstract fun bindStorageRepository(impl: SimpleStorageImpl): StorageRepository
}
