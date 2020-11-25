package com.androiddevs.mvvmnewsapp.ui.viewmodels.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.ui.viewmodels.NewsHomeViewModel
import java.lang.IllegalStateException

@Suppress("UNCHECKED_CAST")
class NewsViewModelFactory(val newsRepo : NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(NewsHomeViewModel::class.java.isAssignableFrom(modelClass)){
            return NewsHomeViewModel(newsRepo) as T
        }
        throw IllegalStateException("Requested ViewModel Doesn't found")
    }
}