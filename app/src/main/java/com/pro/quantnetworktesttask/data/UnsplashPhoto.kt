package com.pro.quantnetworktesttask.data

data class UnsplashPhoto(
    val id: String,
    val create_at: String,
    val width: Int,
    val height: Int,
    val blur_hash: String,
    val description: String?,
    val urls: UnsplashUrls,
    val user: UnsplashUser
)
