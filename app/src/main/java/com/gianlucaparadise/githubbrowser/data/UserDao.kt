package com.gianlucaparadise.githubbrowser.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAll(): List<User>
}