package com.vlu.newsreader.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vlu.newsreader.R
import com.vlu.newsreader.databinding.ItemNewsBinding
import com.vlu.newsreader.model.NewsItem

class NewsAdapter :
    ListAdapter<NewsItem, NewsAdapter.NewsViewHolder>(NewsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NewsViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewsItem) {
            val context = binding.root.context
            binding.tvPostId.text = context.getString(R.string.post_number, item.id)
            binding.tvUserId.text = context.getString(R.string.user_number, item.userId)
            binding.tvTitle.text = item.title
            binding.tvBody.text = item.body
        }
    }

    private companion object {
        val NewsDiffCallback = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
