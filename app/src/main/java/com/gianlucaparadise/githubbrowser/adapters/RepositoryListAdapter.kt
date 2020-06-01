package com.gianlucaparadise.githubbrowser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.data.Repo
import com.gianlucaparadise.githubbrowser.databinding.RepositoryListItemBinding

typealias RepoClickHandler = (Repo) -> Unit

class RepositoryListAdapter(
    val showOwner: Boolean,
    private val onRepoClicked: RepoClickHandler?
) :
    PagedListAdapter<Repo, RepositoryListAdapter.ViewHolder>(RepositoryDiffCallback()) {

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
        getItem(position)?.let { repo ->
            holder.bind(repo, showOwner, createOnClickListener(repo))
        }
    }

    private fun createOnClickListener(repo: Repo): View.OnClickListener {
        return View.OnClickListener {
            onRepoClicked?.invoke(repo)
        }
    }

    class ViewHolder(
        private val binding: RepositoryListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            currentRepo: Repo,
            currentShowOwner: Boolean,
            listener: View.OnClickListener
        ) {
            with(binding) {
                repo = currentRepo
                showOwner = currentShowOwner
                clickListener = listener
                executePendingBindings()
            }
        }
    }
}

private class RepositoryDiffCallback : DiffUtil.ItemCallback<Repo>() {

    override fun areItemsTheSame(
        oldItem: Repo,
        newItem: Repo
    ): Boolean {
        return oldItem.name == newItem.name && oldItem.owner?.login == newItem.owner?.login
    }

    override fun areContentsTheSame(
        oldItem: Repo,
        newItem: Repo
    ): Boolean {
        return oldItem == newItem
    }
}