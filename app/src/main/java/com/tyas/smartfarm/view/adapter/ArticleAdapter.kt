package com.tyas.smartfarm.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tyas.smartfarm.R
import com.tyas.smartfarm.model.Article

class ArticleAdapter(private var articles: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val articleImage: ImageView = view.findViewById(R.id.iv_article_image)
        val articleTitle: TextView = view.findViewById(R.id.tv_plant_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]

        holder.articleTitle.text = article.commonName ?: "Nama Tanaman Tidak Diketahui"

        Glide.with(holder.itemView.context)
            .load(article.defaultImage?.mediumUrl ?: R.drawable.placeholder_image)
            .into(holder.articleImage)
    }

    override fun getItemCount(): Int = articles.size

    fun setArticles(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}
