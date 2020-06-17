package com.gianlucaparadise.githubbrowser.inMemory

import com.gianlucaparadise.githubbrowser.vo.Repo

class RepoInMemoryDao: InMemoryDao<Repo> {

    override val data: List<Repo>
        get() = _data

    private val _data = mutableListOf<Repo>()

    override fun loadInitial(data: List<Repo>) {
        _data.clear()
        _data.addAll(data)
    }

    override fun append(data: List<Repo>) {
        _data.clear()
        _data.addAll(data)
    }

}