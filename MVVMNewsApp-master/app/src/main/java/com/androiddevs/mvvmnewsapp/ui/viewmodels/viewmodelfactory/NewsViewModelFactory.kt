package com.androiddevs.mvvmnewsapp.ui.viewmodels.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.ui.viewmodels.BaseApplicationContext
import com.androiddevs.mvvmnewsapp.ui.viewmodels.NewsHomeViewModel
import java.lang.IllegalStateException

@Suppress("UNCHECKED_CAST")
class NewsViewModelFactory(val applicationContext: Application, val newsRepo : NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(NewsHomeViewModel::class.java.isAssignableFrom(modelClass)){
            return NewsHomeViewModel(applicationContext, newsRepo) as T
        }
        throw IllegalStateException("Requested ViewModel Doesn't found")
    }
}