package com.androiddevs.mvvmnewsapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.models.Article
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article_preview.view.*


class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val diffUtilObj = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val asyncListDiffer = AsyncListDiffer(this, diffUtilObj)

    inner class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(item.urlToImage).into(ivArticleImage)
            tvSource.text = item.source.name
            tvTitle.text = item.title
            tvDescription.text = item.description
            tvPublishedAt.text = item.publishedAt
             setOnClickListener{
                 Log.d("AdityaResult", "adapter clicklistenr $it")
                 onItemClickListener?.let {
                     Log.d("AdityaResult", "adapter clicklistenr $it and ${it(item)}")
                     it(item)
                 }
             }
        }


        }
    private var onItemClickListener : ((Article) -> Unit)? = null

    fun setOnClickListener(listener : (Article) -> Unit){
        Log.d("AdityaResult", "adapter setOncclicklistenr $listener and $onItemClickListener }")
        onItemClickListener = listener
        Log.d("AdityaResullt", "adapter setOncclicklistenr 22222 $listener and $onItemClickListener }")
    }
}