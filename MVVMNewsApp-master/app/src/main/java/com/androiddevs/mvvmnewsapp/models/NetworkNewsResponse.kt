package com.androiddevs.mvvmnewsapp.models


data class NetworkNewsResponseContainer(
    val networkNewsResponse: List<NetworkNewsResponse>
)


data class NetworkNewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)