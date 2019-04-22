package com.garymcgowan.postapocalypse

import android.content.Context
import com.garymcgowan.postapocalypse.core.AppSchedulerProvider
import com.garymcgowan.postapocalypse.core.SchedulerProvider
import com.garymcgowan.postapocalypse.network.NetworkModule
import com.garymcgowan.postapocalypse.storage.StorageModule
import com.garymcgowan.postapocalypse.view.base.ViewModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        ViewModule::class,
        NetworkModule::class,
        StorageModule::class
    ]
)
class AppModule {


    @Provides
    @Singleton
    fun providesAppContext(application: App): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providesSchedulers(): SchedulerProvider = AppSchedulerProvider()
}