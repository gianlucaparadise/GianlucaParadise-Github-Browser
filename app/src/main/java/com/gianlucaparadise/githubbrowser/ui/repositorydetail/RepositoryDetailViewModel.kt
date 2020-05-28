package com.gianlucaparadise.githubbrowser.ui.repositorydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gianlucaparadise.githubbrowser.data.Repository


class RepositoryDetailViewModel(val repository: Repository) : ViewModel() {

    val ownerNameAndLoginid: String?
        get() {
            val owner = repository.owner
            if (owner == null) return null

            if (owner.name != null) {
                return "${owner.name} (${owner.login})"
            }

            return owner.login
        }

    class Factory(
        private val repository: Repository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RepositoryDetailViewModel(repository) as T
        }
    }
}
