package com.assessment.nytimessampleapp.features.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assessment.nytimessampleapp.R
import com.assessment.nytimessampleapp.databinding.ActivityMainBinding
import com.assessment.nytimessampleapp.features.detail.DetailActivity
import com.assessment.nytimessampleapp.features.main.adapters.NewsAdapter
import com.assessment.nytimessampleapp.models.NewsModel
import com.assessment.nytimessampleapp.utils.DaysCount
import com.assessment.nytimessampleapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setupRecyclerView()
        subscribeToObservers()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(::onNewsItemClick)
        binding.newsRecyclerView.adapter = newsAdapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshNews()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        scrollToTopList()
    }

    private fun onNewsItemClick(newsItem: NewsModel) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("news", newsItem)
        startActivity(intent)
    }

    private fun scrollToTopList() {
        newsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                (binding.newsRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                    0,
                    0
                )
            }
        })
    }

    private fun subscribeToObservers() {
        viewModel.latestNews.observe(this, Observer { resource ->
            Timber.d("The current state is $resource")
            when (resource) {
                is Resource.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Resource.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    scrollToTopList()
                    newsAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_one_day -> {
                viewModel.getLatestNews(DaysCount.ONE_DAY)
                true
            }
            R.id.action_seven_days -> {
                viewModel.getLatestNews(DaysCount.SEVEN_DAYS)
                true
            }
            R.id.action_thirty_days -> {
                viewModel.getLatestNews(DaysCount.THIRTY_DAYS)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}