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
    var breakingPageNumber = 1
    private var breakingNewsRecord : NetworkNewsResponse? = null

    var searchPageNumber = 1
    private var searchNewsRecord : NetworkNewsResponse? = null

    fun getTopHeadlines(country : String) = viewModelScope.launch {
        topHealdines.postValue(Resource.Loading())
        val response = newsRepo.getTopHeadlinesData(country, breakingPageNumber)
        Log.d("NewsHomeViewModel", "********** ${response.body()?.articles?.size}")
        Log.d("NewsHomeViewModel", "********** ${response.isSuccessful}")
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
                breakingPageNumber++
                if(breakingNewsRecord == null){
                    Log.d("NewsHomeViewModel", "first response == == = = = ${data.articles.size}")
                    breakingNewsRecord = data
                }else {
                    val oldArticles = breakingNewsRecord?.articles
                    val newArticles = data.articles
                    oldArticles?.addAll(newArticles)
                    Log.d("NewsHomeViewModel", "articles == == = = = ${oldArticles?.size} and new data   " +
                            "${data.articles.size}")
                    Log.d("NewsHomeViewModel", "breakingnewsRecord == == = = = ${breakingNewsRecord} and new data   " +
                            "${data}")
                }
                return Resource.Success(breakingNewsRecord ?: data)
            }
        }
        return Resource.Error(response.message())
    }

    private fun responseSearchResult(response : Response<NetworkNewsResponse>) : Resource<NetworkNewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { data ->
                if(searchNewsRecord == null){
                    searchNewsRecord = data
                } else{
                    val oldArticles = searchNewsRecord?.articles
                    val newArticles = data.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsRecord ?: data)
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