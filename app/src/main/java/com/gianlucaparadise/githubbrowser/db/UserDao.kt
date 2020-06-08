package com.gianlucaparadise.githubbrowser.db

import androidx.room.Dao
import androidx.room.Query
import com.gianlucaparadise.githubbrowser.vo.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAll(): List<User>
}