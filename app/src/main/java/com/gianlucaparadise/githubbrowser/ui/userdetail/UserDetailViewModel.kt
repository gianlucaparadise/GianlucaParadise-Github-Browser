package com.gianlucaparadise.githubbrowser.ui.userdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gianlucaparadise.githubbrowser.vo.User

class UserDetailViewModel @ViewModelInject constructor () : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun start(inputUser: User) { // This should be a constructor parameter
        _user.value = inputUser
    }
}
