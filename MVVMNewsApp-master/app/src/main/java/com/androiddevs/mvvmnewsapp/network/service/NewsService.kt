package com.androiddevs.mvvmnewsapp.network.service

import com.androiddevs.mvvmnewsapp.NetworkNewsResponseContainer
import com.androiddevs.mvvmnewsapp.network.NetworkConstants.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        countryName : String = "india",
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = ApiConstants.NEWS_API
    ) : Response<NetworkNewsResponseContainer>

    suspend fun search(
        @Query("q")
        searchNews : String,
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = ApiConstants.NEWS_API
    ) : Response<NetworkNewsResponseContainer>
}