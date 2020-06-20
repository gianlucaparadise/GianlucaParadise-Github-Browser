package com.gianlucaparadise.githubbrowser.inMemory

interface InMemoryDao<T> {
    fun getAll(): List<T>
    /**
     * Get last pagination cursor
     */
    fun getNextKey(): String?
    fun loadInitial(data: List<T>)
    fun append(data: List<T>)
    fun update(data: T)
}