package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.viewmodels.NewsHomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleView : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsHomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        val args = ArticleViewArgs.fromBundle(arguments!!).article

        webView.apply {
            webViewClient  = WebViewClient()
            loadUrl(args.url)
        }

        fab.setOnClickListener {
            viewModel.insertArticle(args)
            Snackbar.make(view, "Article Saved Sucessfully..", Snackbar.LENGTH_SHORT).show()
        }
    }
}