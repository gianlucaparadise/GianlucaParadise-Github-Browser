package com.gianlucaparadise.githubbrowser.inMemory

import com.gianlucaparadise.githubbrowser.vo.Repo

class RepoInMemoryDao: InMemoryDao<Repo> {

    val data: List<Repo>
        get() = _data

    private val _data = mutableListOf<Repo>()

    override fun getAll(): List<Repo> = data

    override fun getNextKey(): String? = data.lastOrNull()?.paginationCursor

    override fun loadInitial(data: List<Repo>) {
        _data.clear()
        _data.addAll(data)
    }

    override fun append(data: List<Repo>) {
        _data.addAll(data)
    }

    override fun update(data: Repo) {
        val index = _data.indexOfFirst { it.id == data.id }
        if (index < 0) return

        _data[index] = data
    }
}