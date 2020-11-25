package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.data.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.ui.viewmodels.NewsHomeViewModel
import com.androiddevs.mvvmnewsapp.ui.viewmodels.viewmodelfactory.NewsViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel : NewsHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = ArticleDatabase.getInstance(this)

        val newsRepo = NewsRepository(database)

        val newsViewModelFactory = NewsViewModelFactory(newsRepo)

        viewModel = ViewModelProvider(this, newsViewModelFactory).get(NewsHomeViewModel::class.java)

        bottomNavigationView.setupWithNavController(navHostFramgent.findNavController())
    }
}
