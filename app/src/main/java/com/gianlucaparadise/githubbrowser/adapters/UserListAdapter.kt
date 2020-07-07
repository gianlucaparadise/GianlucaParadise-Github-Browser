package com.gianlucaparadise.githubbrowser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.vo.User
import com.gianlucaparadise.githubbrowser.databinding.UserListItemBinding

typealias UserClickHandler = (User, holder: UserListAdapter.ViewHolder) -> Unit

class UserListAdapter(private val onUserClicked: UserClickHandler?) :
    PagedListAdapter<User, UserListAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.user_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { user ->
            holder.bind(user, createOnClickListener(user, holder))
        }
    }

    private fun createOnClickListener(user: User, holder: ViewHolder): View.OnClickListener {
        return View.OnClickListener {
            onUserClicked?.invoke(user, holder)
        }
    }

    class ViewHolder(
        private val binding: UserListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            currentUser: User,
            listener: View.OnClickListener
        ) {
            with(binding) {
                user = currentUser
                clickListener = listener
                executePendingBindings()
            }
        }
    }
}

private class UserDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean {
        return oldItem.login == newItem.login
    }

    override fun areContentsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean {
        return oldItem == newItem
    }
}