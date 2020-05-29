package com.gianlucaparadise.githubbrowser.ui.userdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gianlucaparadise.githubbrowser.data.User

class UserDetailViewModel(inputUser: User) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init {
        _user.value = inputUser
    }

    class Factory(
        private val user: User
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserDetailViewModel(user) as T
        }
    }
}
