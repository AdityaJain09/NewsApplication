package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapter.NewsAdapter
import com.androiddevs.mvvmnewsapp.network.NetworkConstants.GeneralConstants.Companion.SEARCH_NEWS_DELAY
import com.androiddevs.mvvmnewsapp.ui.viewmodels.NewsHomeViewModel
import com.androiddevs.mvvmnewsapp.ui.viewmodels.Resource
import com.google.android.material.snackbar.Snackbar
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
                        newsAdapter.asyncListDiffer.submitList(networkNewsResponse.articles.toList())
                        val totalResult = networkNewsResponse.totalResults / 20 + 2
                        isLastPage = totalResult == viewModel.searchPageNumber
                        if (isLastPage) {
                            rvSearchNews.setPadding(0, 0, 0, 0)
                        }
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

    var isLoading = false
    var isScrolling = false
    var isLastPage  = false

    val scorllListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val visibleItems  = layoutManager.childCount
            val TotalItems = layoutManager.itemCount
            val lasttVisibleItem  = layoutManager.findLastVisibleItemPosition()

            val isNotLodingNotLastPage = !isLoading && !isLastPage
            val isLastItem =  lasttVisibleItem + 1 >= TotalItems
            val isTotalMoreThanVisible = TotalItems >= 20
            val shouldPaginate = isNotLodingNotLastPage && isTotalMoreThanVisible && isLastItem
            Log.d("MainActivity", "checking params $isNotLodingNotLastPage and" +
                    " $isTotalMoreThanVisible  and $isLastItem and $lasttVisibleItem")
            if(shouldPaginate){
                viewModel.getSearchTopHeadlines("us")
                isScrolling = false
            }
        }
    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun recyleViewSetup(){
        newsAdapter = NewsAdapter()
        rvSearchNews?.apply {
            adapter  = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scorllListener)
        }
    }
}