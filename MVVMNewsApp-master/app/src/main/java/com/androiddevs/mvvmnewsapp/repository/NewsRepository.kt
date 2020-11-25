package com.androiddevs.mvvmnewsapp.repository

import com.androiddevs.mvvmnewsapp.data.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NetworkNewsResponse
import com.androiddevs.mvvmnewsapp.network.RetrofitApi
import retrofit2.Response

class NewsRepository(val database : ArticleDatabase) {

    suspend fun getTopHeadlinesData(country : String, pageNumber : Int) =
       RetrofitApi.getRetrofitInstance.getTopHeadlines(country, pageNumber)

    suspend fun searchNews(searchString : String, pageNumber: Int) : Response<NetworkNewsResponse> {
       return RetrofitApi.getRetrofitInstance.search(searchString, pageNumber)
    }

    suspend fun insertToDatabase(article: Article) = database.articleDao.insert(article)

    fun getAllSavedArticles() = database.articleDao.getAllArticles()

    suspend fun deleteArticle(article: Article) = database.articleDao.delete(article)
}