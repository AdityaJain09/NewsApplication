package com.androiddevs.mvvmnewsapp.util

sealed class Resource<T>(
    val  data : T? = null,
    val message : String? = null
) {
    class Success<T>(data : T) : Resource<T>(data)
}