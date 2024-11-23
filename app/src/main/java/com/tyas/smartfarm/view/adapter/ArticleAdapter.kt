package com.tyas.smartfarm.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tyas.smartfarm.R
import com.tyas.smartfarm.model.Article

class ArticleAdapter(private val articles: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val articleImage: ImageView = view.findViewById(R.id.iv_article_image)
        val articleTitle: TextView = view.findViewById(R.id.tv_article_title)
        val articleDescription: TextView = view.findViewById(R.id.tv_article_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.articleImage.setImageResource(article.imageResId)
        holder.articleTitle.text = article.title
        holder.articleDescription.text = article.description
    }

    override fun getItemCount(): Int = articles.size
}
