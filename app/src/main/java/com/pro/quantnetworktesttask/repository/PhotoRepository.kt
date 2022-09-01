package com.pro.quantnetworktesttask.repository

import android.util.Log
import com.google.gson.GsonBuilder
import com.pro.quantnetworktesttask.data.UnsplashPhoto
import com.pro.quantnetworktesttask.remote.RemotePhotoList
import com.pro.quantnetworktesttask.remote.UnsplashApi
import com.pro.quantnetworktesttask.utils.Constants.CASH_PAGE_1
import com.pro.quantnetworktesttask.utils.Constants.CASH_PAGE_2
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val api: UnsplashApi
) {

//    suspend fun getPhotoList(page: Int = 1): Resource<RemotePhotoList> {
//        val response = try {
//            api.getPhotoList(page)
//        } catch (e: Exception) {
//            val textErr = if(e.message.isNullOrBlank()) {"An unknown error occurred."} else e.message!!
//            return Resource.Error(textErr)
//        }
//        return Resource.Success(response)
//    }

    suspend fun getPhotoList(page: Int = 1): Response<List<UnsplashPhoto>> = api.getPhotoList(page)

    suspend fun getTestPhotoList(page: Int = 1): Response<List<UnsplashPhoto>> {
        delay(1000)

        val json = if(page == 1) CASH_PAGE_1 else CASH_PAGE_2
        val g = GsonBuilder().create()
        val photoList = g.fromJson(json, RemotePhotoList::class.java)
        Log.d("Repository","photoList ${photoList.size}")
        return Response.success(photoList)
    }

}