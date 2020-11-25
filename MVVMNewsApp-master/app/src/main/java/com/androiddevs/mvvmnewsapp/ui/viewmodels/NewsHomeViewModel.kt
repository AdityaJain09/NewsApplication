package com.androiddevs.mvvmnewsapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NetworkNewsResponse
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsHomeViewModel(private val newsRepo : NewsRepository) : ViewModel() {
    val topHealdines :  MutableLiveData<Resource<NetworkNewsResponse>> = MutableLiveData()
    val searchTopHealdines :  MutableLiveData<Resource<NetworkNewsResponse>> = MutableLiveData()

    init {
        getTopHeadlines("us")
    }
    private val pageNumber = 1
    private val searchPageNumber = 1

    fun getTopHeadlines(country : String) = viewModelScope.launch {
        topHealdines.postValue(Resource.Loading())
        val response = newsRepo.getTopHeadlinesData(country, pageNumber)
        topHealdines.postValue(responseArticleResult(response))
    }

    fun getSearchTopHeadlines(searchString : String) = viewModelScope.launch {
        searchTopHealdines.postValue(Resource.Loading())
        val response = newsRepo.searchNews(searchString, searchPageNumber)
        searchTopHealdines.postValue(responseSearchResult(response))
    }

    private fun responseArticleResult(response : Response<NetworkNewsResponse>) : Resource<NetworkNewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        return Resource.Error(response.message())
    }

    private fun responseSearchResult(response : Response<NetworkNewsResponse>) : Resource<NetworkNewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        return Resource.Error(response.message())
    }

    fun insertArticle(article: Article) = viewModelScope.launch {
        newsRepo.insertToDatabase(article)
    }

    fun getAllSavedArticles() = newsRepo.getAllSavedArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepo.deleteArticle(article)
    }
}