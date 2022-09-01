package com.pro.quantnetworktesttask.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pro.quantnetworktesttask.repository.PhotoRepository
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.lang.Exception

class UnsplashPhotosPagingDataSource(
    private val repository: PhotoRepository,
) : PagingSource<Int, UnsplashPhoto>() {

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val pageNumber = params.key ?: 0

        Timber.d("load page")
        val response = try {
            repository.getPhotoList(pageNumber)
        }
        catch (e: Exception) {
            Timber.d("network error")
            if (e is IOException)
                return LoadResult.Error(Exception("Network error. Check your internet connection."))
            return LoadResult.Error(e)
        }

        return if (response.isSuccessful) {
            val data = response.body() ?: listOf()
            // increase page number
            val nextPage = pageNumber + 1
            Timber.d("nextPage $nextPage")
            // if pageNumber is greater than 0, then this isn't the first page
            LoadResult.Page(
                data = data,
                prevKey = if (pageNumber > 0) pageNumber - 1 else null,
                nextKey = nextPage
            )
        } else {
            Timber.d("error load")
            LoadResult.Error(HttpException(response))
        }
    }


}