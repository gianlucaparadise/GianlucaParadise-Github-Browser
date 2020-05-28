package com.gianlucaparadise.githubbrowser.ui.repositorydetail

import androidx.lifecycle.*
import com.gianlucaparadise.githubbrowser.data.Repository
import com.gianlucaparadise.githubbrowser.network.BackendService
import kotlinx.coroutines.launch


class RepositoryDetailViewModel(inputRepository: Repository) : ViewModel() {

    private val _repository = MutableLiveData<Repository>()
    val repository: LiveData<Repository> = _repository

    private val _ownerNameAndLoginid = MutableLiveData<String>()
    val ownerNameAndLoginid: LiveData<String> = _ownerNameAndLoginid

    init {
        updateRepository(inputRepository)
    }

    private fun updateRepository(repository: Repository) {
        _repository.value = repository
        _ownerNameAndLoginid.value = if (repository.owner?.name != null) {
            "${repository.owner.name} (${repository.owner.login})"
        } else {
            repository.owner?.login
        }
    }

    fun onClickStar() {
        viewModelScope.launch {
            val repository = _repository.value

            if (repository != null) {
                val starrable = BackendService.toggleStar(repository)
                if (starrable != null) {
                    val newRepo = repository.copy(
                        stargazersCount = starrable.stargazersCount,
                        viewerHasStarred = starrable.viewerHasStarred
                    )
                    updateRepository(newRepo)
                }
            }
        }
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
