package com.gianlucaparadise.githubbrowser.inMemory

interface InMemoryDao<T> {
    val data: List<T>

    fun loadInitial(data: List<T>)
    fun append(data: List<T>)
}