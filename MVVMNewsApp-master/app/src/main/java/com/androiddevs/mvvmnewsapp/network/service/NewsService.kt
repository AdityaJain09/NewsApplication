package com.androiddevs.mvvmnewsapp.network.service

import com.androiddevs.mvvmnewsapp.models.NetworkNewsResponse
import com.androiddevs.mvvmnewsapp.models.NetworkNewsResponseContainer
import com.androiddevs.mvvmnewsapp.network.NetworkConstants.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        countryName : String,
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = ApiConstants.NEWS_API
    ) : Response<NetworkNewsResponse>

    @GET("v2/everything")
    suspend fun search(
        @Query("q")
        searchNews : String,
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = ApiConstants.NEWS_API
    ) : Response<NetworkNewsResponse>
}