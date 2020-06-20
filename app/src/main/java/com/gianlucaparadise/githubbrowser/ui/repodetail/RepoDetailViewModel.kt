package com.gianlucaparadise.githubbrowser.ui.repodetail

import androidx.lifecycle.*
import com.gianlucaparadise.githubbrowser.vo.Repo
import com.gianlucaparadise.githubbrowser.network.BackendService
import com.gianlucaparadise.githubbrowser.repository.GithubRepository
import kotlinx.coroutines.launch

class RepoDetailViewModel(inputRepo: Repo) : ViewModel() {

    private val _repo = MutableLiveData<Repo>()
    val repo: LiveData<Repo> = _repo

    private val _ownerNameAndLoginid = MutableLiveData<String>()
    val ownerNameAndLoginid: LiveData<String> = _ownerNameAndLoginid

    init {
        updateRepo(inputRepo)
    }

    private fun updateRepo(repo: Repo) {
        _repo.value = repo
        _ownerNameAndLoginid.value = if (repo.owner?.name != null) {
            "${repo.owner.name} (${repo.owner.login})"
        } else {
            repo.owner?.login
        }
    }

    private fun updateDbRepo(repo: Repo) {
        viewModelScope.launch {
            GithubRepository.instance.updateRepo(repo)
        }
    }

    fun onClickStar() {
        viewModelScope.launch {
            val repo = _repo.value

            if (repo != null) {
                val starrable = BackendService.toggleStar(repo)
                if (starrable != null) {
                    val newRepo = repo.copy(
                        stargazersCount = starrable.stargazersCount,
                        viewerHasStarred = starrable.viewerHasStarred
                    )
                    updateRepo(newRepo)
                    updateDbRepo(newRepo)
                }
            }
        }
    }

    class Factory(
        private val repo: Repo
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RepoDetailViewModel(repo) as T
        }
    }
}
