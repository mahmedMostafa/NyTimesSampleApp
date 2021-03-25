package com.assessment.nytimessampleapp.features.detail

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.assessment.nytimessampleapp.R
import com.assessment.nytimessampleapp.databinding.ActivityDetailBinding
import com.assessment.nytimessampleapp.models.NewsModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var newsModel: NewsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent?.let {
            newsModel = it.getSerializableExtra("news") as NewsModel
        }
        init()
    }

    private fun init() {
        bindProperties()
    }

    private fun bindProperties(){
        binding.newsTitleTextView.text = newsModel.title
        binding.newsDateTextView.text = newsModel.publishedDate
        binding.newsBodyTextView.text = newsModel.abstract
        binding.newsWriterTextView.text = newsModel.byline
    }
}