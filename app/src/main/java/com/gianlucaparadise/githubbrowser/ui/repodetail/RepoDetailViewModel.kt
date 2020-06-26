package com.gianlucaparadise.githubbrowser.ui.repodetail

import android.util.Log
import androidx.lifecycle.*
import com.gianlucaparadise.githubbrowser.vo.Repo
import com.gianlucaparadise.githubbrowser.network.BackendService
import com.gianlucaparadise.githubbrowser.repository.GithubRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class RepoDetailViewModel(inputRepo: Repo) : ViewModel() {

    companion object {
        const val tag = "RepoDetailViewModel"
    }

    private val _repo = MutableLiveData<Repo>()
    val repo: LiveData<Repo> = _repo

    private val _ownerNameAndLoginid = MutableLiveData<String>()
    val ownerNameAndLoginid: LiveData<String> = _ownerNameAndLoginid

    private val _canStar = MutableLiveData<Boolean>(true)
    val canStar: LiveData<Boolean> = _canStar

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
        val repo = _repo.value ?: return

        viewModelScope.launch {
            try {
                _canStar.value = false

                val starrable = BackendService.toggleStar(repo)
                if (starrable != null) {
                    val newRepo = repo.copy(
                        stargazersCount = starrable.stargazersCount,
                        viewerHasStarred = starrable.viewerHasStarred
                    )
                    updateRepo(newRepo)
                    updateDbRepo(newRepo)
                }

                _canStar.value = true
            }
            catch (ex: Exception) {
                Log.e(tag, "Exception while toggling the star: $ex")
                _canStar.value = true
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
