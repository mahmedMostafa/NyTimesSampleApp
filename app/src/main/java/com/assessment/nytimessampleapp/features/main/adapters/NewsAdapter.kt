package com.assessment.nytimessampleapp.features.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.assessment.nytimessampleapp.databinding.RowNewsBinding
import com.assessment.nytimessampleapp.models.NewsModel

class NewsAdapter(
    private val onNewsItemClick: (NewsModel) -> Unit
) : ListAdapter<NewsModel, NewsViewHolder>(NewsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            RowNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            , onNewsItemClick
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class NewsDiffUtil : DiffUtil.ItemCallback<NewsModel>() {

        override fun areItemsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
            return oldItem == newItem
        }

    }
}