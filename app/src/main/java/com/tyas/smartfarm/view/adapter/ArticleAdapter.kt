package com.tyas.smartfarm.view.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.ItemArticleBinding
import com.tyas.smartfarm.model.Article

class ArticleAdapter(private var articles: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]

        // Set artikel data
        holder.binding.apply {
            tvPlantName.text = article.commonName ?: "Nama Tanaman Tidak Diketahui"
            tvScientificName.text = article.scientificName?.joinToString(", ") ?: "Nama Ilmiah Tidak Diketahui"
            tvLifeCycle.text = "Siklus Hidup: ${article.cycle ?: "Tidak Diketahui"}"
            tvWatering.text = "Perawatan: ${article.watering ?: "Tidak Diketahui"}"
            tvSunlight.text = "Cahaya: ${article.sunlight?.joinToString(", ") ?: "Tidak Diketahui"}"

            // Load image dengan Glide
            Glide.with(root.context)
                .load(article.defaultImage?.mediumUrl ?: R.drawable.placeholder_image)
                .into(ivArticleImage)

            // Handle klik tombol "Lihat Artikel"
            btnLook.setOnClickListener {
                val context = root.context
                val url = "https://www.google.com"

                try {
                    Log.d("ArticleAdapter", "Opening URL: $url")
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(url)
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e("ArticleAdapter", "Error opening URL: ${e.message}", e)
                    Toast.makeText(context, "Browser tidak ditemukan atau tidak dapat membuka URL.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = articles.size

    fun setArticles(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}
