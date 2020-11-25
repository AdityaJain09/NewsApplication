package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapter.NewsAdapter
import com.androiddevs.mvvmnewsapp.network.NetworkConstants.GeneralConstants.Companion.SEARCH_NEWS_DELAY
import com.androiddevs.mvvmnewsapp.ui.viewmodels.NewsHomeViewModel
import com.androiddevs.mvvmnewsapp.ui.viewmodels.Resource
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    lateinit var newsAdapter : NewsAdapter
    lateinit var viewModel: NewsHomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        recyleViewSetup()

        newsAdapter.setOnClickListener {
            it.let {
                this.findNavController().navigate(
                   SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleView(it)
                )
            }
        }

        var job : Job? = null

        etSearch.addTextChangedListener {
            Log.d("SearchNewsFragment","are you listneer search . . . . . ")
            job?.cancel()
            job = MainScope().launch {
             delay(SEARCH_NEWS_DELAY)
                if(etSearch.text.toString().isNotEmpty()){
                    viewModel.getSearchTopHeadlines(etSearch.text.toString())
                }
            }
        }

        viewModel.searchTopHealdines.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {networkNewsResponse ->
                        newsAdapter.asyncListDiffer.submitList(networkNewsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { errorMessage ->
                        Log.d("SearchNewsFragment", "message -> $errorMessage")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun recyleViewSetup(){
        newsAdapter = NewsAdapter()
        rvSearchNews?.apply {
            adapter  = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}