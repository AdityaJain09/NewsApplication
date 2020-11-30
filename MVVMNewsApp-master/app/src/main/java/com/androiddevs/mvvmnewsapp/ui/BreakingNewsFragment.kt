package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        newsAdapter.setOnClickListener {
            it.let {
                if(findNavController().currentDestination?.id == R.id.breakingNewsFragment) {
                    findNavController().navigate(
                        BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleView(it)
                    )
                }
            }
        }


        viewModel.topHealdines.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {networkNewsResponse ->
                        Log.d("BreakingNewsFragment", "total no of data = ${networkNewsResponse.articles.size}")
                         newsAdapter.asyncListDiffer.submitList(networkNewsResponse.articles)
                        val totalPages = networkNewsResponse.totalResults / 20 + 2
                        islastPage = totalPages == viewModel.breakingPageNumber

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

    var isLoading = false
    var isScrolling = false
    var islastPage = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            Log.d("MainActivity", "scorll state = $newState")
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            val totalItem = layoutManager.itemCount
            val totalVisibleItem = layoutManager.childCount
            val isLastItem = firstVisibleItem + totalVisibleItem >= totalItem
            val isTotalItemsMoreThanVisible = totalItem >= 20
                val isLoadingAndNotLastPage = !isLoading && !islastPage
            val shouldPaginate = isLastItem && isLoadingAndNotLastPage && isScrolling && isTotalItemsMoreThanVisible

            Log.d("MainActivity", " islastitem = $isLastItem" +
                    " isloadingandnotlsatpage = $isLoadingAndNotLastPage " +
                    "isscrollng = $isScrolling" +
                    " is TotalItemsMore = $isTotalItemsMoreThanVisible")

            Log.d("MainActiavity"," isloading = $isLoading and is lsatpage = $islastPage")

            if(shouldPaginate){
                viewModel.getTopHeadlines("us")
                isScrolling = false
            }else{
                Log.d("MainActivity", "pagainate or not  === $shouldPaginate")
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
        rvBreakingNews?.apply {
            adapter  = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

}