package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapter.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.viewmodels.NewsHomeViewModel
import com.androiddevs.mvvmnewsapp.ui.viewmodels.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news){

    lateinit var viewModel: NewsHomeViewModel
    lateinit var newsAdapter : NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        recyleViewSetup()

        viewModel.topHealdines.observe(viewLifecycleOwner, Observer { response ->
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
                        Log.d("BreakingNewsFragment", "message -> $errorMessage")
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
        rvBreakingNews?.apply {
            adapter  = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}