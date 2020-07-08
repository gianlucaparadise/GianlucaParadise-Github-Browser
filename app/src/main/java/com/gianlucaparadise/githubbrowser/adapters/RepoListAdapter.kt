package com.gianlucaparadise.githubbrowser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.vo.Repo
import com.gianlucaparadise.githubbrowser.databinding.RepoListItemBinding

typealias RepoClickHandler = (Repo, RepoListAdapter.ViewHolder) -> Unit

class RepoListAdapter(
    val showOwner: Boolean,
    private val onRepoClicked: RepoClickHandler?
) :
    PagedListAdapter<Repo, RepoListAdapter.ViewHolder>(RepoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.repo_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { repo ->
            holder.bind(repo, showOwner, createOnClickListener(repo, holder))
        }
    }

    private fun createOnClickListener(repo: Repo, holder: ViewHolder): View.OnClickListener {
        return View.OnClickListener {
            onRepoClicked?.invoke(repo, holder)
        }
    }

    class ViewHolder(
        private val binding: RepoListItemBinding
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

private class RepoDiffCallback : DiffUtil.ItemCallback<Repo>() {

    override fun areItemsTheSame(
        oldItem: Repo,
        newItem: Repo
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Repo,
        newItem: Repo
    ): Boolean {
        return oldItem.name == newItem.name &&
                oldItem.owner?.login == newItem.owner?.login &&
                oldItem.stargazersCount == newItem.stargazersCount // this is a value that the user can change
    }
}