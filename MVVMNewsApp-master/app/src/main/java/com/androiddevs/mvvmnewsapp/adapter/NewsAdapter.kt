package com.androiddevs.mvvmnewsapp.adapter

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


class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){

    private val differList by lazy {
        AsyncListDiffer(this, diffUtilObj)
    }

    private val diffUtilObj = object  : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
          return  oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
           return oldItem == newItem
        }
    }

    inner class  NewsViewHolder(view : View) : RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_article_preview,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = differList.currentList.get(position)
        holder.itemView.apply {
            Glide.with(this).load(item.urlToImage).into(ivArticleImage)
            tvSource.text = item.source.name
            tvTitle.text = item.title
            tvDescription.text = item.description
            tvPublishedAt.text = item.publishedAt
            onItemClickedListener?.let { it(item) }
        }
    }

    override fun getItemCount(): Int = differList.currentList.size
}

 private var  onItemClickedListener : ((Article) -> Unit)? = null

 fun setonClickListener(listener : (Article) -> Unit){
    onItemClickedListener = listener
 }
//
//class ArticleClickListener(val onClickListener: (articleId : Int?) -> Unit){
//    fun onClick(article : Article) = onClickListener(article.articleId)
//}
