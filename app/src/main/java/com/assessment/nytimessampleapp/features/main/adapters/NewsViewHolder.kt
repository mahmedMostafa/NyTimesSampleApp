package com.assessment.nytimessampleapp.features.main.adapters

import androidx.recyclerview.widget.RecyclerView
import com.assessment.nytimessampleapp.databinding.RowNewsBinding
import com.assessment.nytimessampleapp.models.NewsModel

class NewsViewHolder(
    private val binding: RowNewsBinding,
    private val onNewsItemClick: (NewsModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: NewsModel) {
        binding.newsTitleTextView.text = item.title
        binding.newsBodyTextView.text = item.abstract
        binding.newsWriterTextView.text = item.byline
        binding.newsDateTextView.text = item.publishedDate
        binding.root.setOnClickListener { onNewsItemClick.invoke(item) }
    }
}