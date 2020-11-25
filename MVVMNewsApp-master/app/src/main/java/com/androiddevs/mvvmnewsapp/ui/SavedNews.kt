package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapter.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.viewmodels.NewsHomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    lateinit var viewModel: NewsHomeViewModel
    lateinit var newsAdapter : NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        recyleViewSetup()

        newsAdapter.setOnClickListener {
            if(findNavController().currentDestination?.id == R.id.savedNewsFragment){
                findNavController().navigate(SavedNewsFragmentDirections
                    .actionSavedNewsFragmentToArticleView(it))
            }
        }

        viewModel.getAllSavedArticles().observe(viewLifecycleOwner, Observer {article ->
            newsAdapter.asyncListDiffer.submitList(article)
        })

        val itemTouchHelper  = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or  ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or  ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView    .ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val getArticle = newsAdapter.asyncListDiffer.currentList[position]
                viewModel.deleteArticle(getArticle)
                Snackbar.make(view, "Article Deleted Successfully", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.insertArticle(getArticle)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(rvSavedNews)
        }
    }

    private fun recyleViewSetup(){
        newsAdapter = NewsAdapter()
        rvSavedNews?.apply {
            adapter  = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}