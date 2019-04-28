package com.garymcgowan.postapocalypse.network

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    @BaseUrlString
    fun provideURL(): String = "http://jsonplaceholder.typicode.com"

    @Provides
    @Singleton
    @AvatarUrlString
    fun provideAvatarURL(): String = "https://api.adorable.io/avatars"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    }.build()


    @Provides
    @Singleton
    fun provideRetrofit(@BaseUrlString url: String, client: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesNetworkRepositor(impl: NetworkRepositoryImpl): NetworkRepository = impl

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): PostsApi {
        return retrofit.create(PostsApi::class.java)

    }

    @Provides
    fun provideImageLoader(impl: ImageLoaderImpl): ImageLoader = impl
}