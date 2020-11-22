package com.androiddevs.mvvmnewsapp.network

import com.androiddevs.mvvmnewsapp.network.NetworkConstants.ApiConstants.Companion.BASE_URL
import com.androiddevs.mvvmnewsapp.network.service.NewsService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApi {
    val retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
    }

    val getRetrofitInstance by lazy {
        retrofit.create(NewsService::class.java)
    }
}