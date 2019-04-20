package com.garymcgowan.postapocalypse

import android.content.Context
import com.garymcgowan.postapocalypse.core.AppSchedulerProvider
import com.garymcgowan.postapocalypse.core.SchedulerProvider
import com.garymcgowan.postapocalypse.network.NetworkModule
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module(
    includes = [
        ActivityModule::class,
        NetworkModule::class
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

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

}