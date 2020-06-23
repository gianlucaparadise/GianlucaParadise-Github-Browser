package com.gianlucaparadise.githubbrowser.inMemory

class InMemoryDao<T> {

    val data: List<T>
        get() = _data

    private val _data = mutableListOf<T>()

    fun getAll(): List<T> = data

    fun loadInitial(data: List<T>) {
        _data.clear()
        _data.addAll(data)
    }

    fun append(data: List<T>) {
        _data.addAll(data)
    }

    fun update(data: T) {
        val index = _data.indexOf(data)
        if (index < 0) return

        _data[index] = data
        onItemUpdated?.invoke(data)
    }

    var onItemUpdated: ((newItem: T) -> Unit)? = null
}