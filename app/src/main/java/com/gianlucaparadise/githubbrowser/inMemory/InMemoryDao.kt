package com.gianlucaparadise.githubbrowser.inMemory

interface InMemoryDao<T> {
    fun getAll(): List<T>
    fun loadInitial(data: List<T>)
    fun append(data: List<T>)
}