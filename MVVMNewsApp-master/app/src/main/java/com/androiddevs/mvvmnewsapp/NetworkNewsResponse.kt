package com.androiddevs.mvvmnewsapp


data class NetworkNewsResponseContainer(
    val networkNewsResponse: List<NetworkNewsResponse>
)


data class NetworkNewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)