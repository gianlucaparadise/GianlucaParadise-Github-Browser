package com.gianlucaparadise.githubbrowser.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.data.Repository
import com.gianlucaparadise.githubbrowser.databinding.RepositoryListItemBinding

class RepositoryListAdapter :
    PagedListAdapter<Repository, RepositoryListAdapter.ViewHolder>(RepositoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.repository_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { repository ->
            holder.bind(repository)
        }
    }

    class ViewHolder(
        private val binding: RepositoryListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentRepository: Repository) {
            with(binding) {
                repository = currentRepository
                executePendingBindings()
            }
        }
    }
}

private class RepositoryDiffCallback : DiffUtil.ItemCallback<Repository>() {

    override fun areItemsTheSame(
        oldItem: Repository,
        newItem: Repository
    ): Boolean {
        return oldItem.name == newItem.name && oldItem.owner?.login == newItem.owner?.login
    }

    override fun areContentsTheSame(
        oldItem: Repository,
        newItem: Repository
    ): Boolean {
        return oldItem == newItem
    }
}