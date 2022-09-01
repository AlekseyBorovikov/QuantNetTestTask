package com.pro.quantnetworktesttask.remote

import com.pro.quantnetworktesttask.data.UnsplashPhoto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    @GET("/photos")
    suspend fun getPhotoList(
        @Query("page") page: Int
    ) : Response<List<UnsplashPhoto>>

}