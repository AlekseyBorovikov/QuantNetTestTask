package com.pro.quantnetworktesttask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pro.quantnetworktesttask.data.UnsplashPhoto
import com.pro.quantnetworktesttask.data.UnsplashPhotosPagingDataSource
import com.pro.quantnetworktesttask.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PhotoRepository
) : ViewModel() {

    private val errorMessageFlow = MutableStateFlow<String?>(null)

    val photos: Flow<PagingData<UnsplashPhoto>> = Pager(
        PagingConfig(pageSize = 10, prefetchDistance = 1,)
    ) { UnsplashPhotosPagingDataSource(repository) }.flow.cachedIn(viewModelScope)

    fun updateErrorMessage(message: String? = null) {
        if (errorMessageFlow.value != message) errorMessageFlow.value = message
    }
}