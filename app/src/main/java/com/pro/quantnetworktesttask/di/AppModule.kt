package com.pro.quantnetworktesttask.di

import com.pro.quantnetworktesttask.remote.UnsplashApi
import com.pro.quantnetworktesttask.repository.PhotoRepository
import com.pro.quantnetworktesttask.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePhotoRepository(api: UnsplashApi) = PhotoRepository(api)

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideVSearchApi(
        httpClient: OkHttpClient
    ): UnsplashApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(httpClient)
            .build()
            .create(UnsplashApi::class.java)
    }

}