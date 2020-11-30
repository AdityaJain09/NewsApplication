package com.androiddevs.mvvmnewsapp.ui.viewmodels

import android.app.ActivityManager
import android.app.Application
import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NetworkNewsResponse
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsHomeViewModel(val applicationContext : Application,
                        private val newsRepo : NewsRepository) : AndroidViewModel(applicationContext) {
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
        getTopHeadlinesOnConnection(country)
        Log.d("NewsHomeViewModel","is checkInternetCOnnectivity****** = ${checkInternetConnectivity()}")
    }

    fun getSearchTopHeadlines(searchString : String) = viewModelScope.launch {
        searchTopHealdines.postValue(Resource.Loading())
        val response = newsRepo.searchNews(searchString, searchPageNumber)
        searchTopHealdines.postValue(responseSearchResult(response))
    }

    private suspend fun getTopHeadlinesOnConnection(country: String) {
        topHealdines.postValue(Resource.Loading())
        Log.d("NewsHomeViewModel","is checkInternetCOnnectivity****** = ${checkInternetConnectivity()}")
        try {
            if (checkInternetConnectivity()) {
                val response = newsRepo.getTopHeadlinesData(country, breakingPageNumber)
                topHealdines.postValue(responseArticleResult(response))
            }
            else{
                topHealdines.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t : Throwable){
            when(t) {
                is IOException -> topHealdines.postValue(Resource.Error("Network Failure"))
                else -> topHealdines.postValue(Resource.Error("Conversion Error"))
            }
        }
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

    private fun checkInternetConnectivity() : Boolean{

      val connectivityManager  = getApplication<BaseApplicationContext>().getSystemService(
          Context.CONNECTIVITY_SERVICE ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capability = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return  when{
                capability.hasTransport(TRANSPORT_WIFI) -> true
                capability.hasTransport(TRANSPORT_CELLULAR) -> true
                capability.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
      return  false
    }
}