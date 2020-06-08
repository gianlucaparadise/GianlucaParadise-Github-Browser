package com.gianlucaparadise.githubbrowser.data

import androidx.paging.DataSource
import androidx.room.*
import com.gianlucaparadise.githubbrowser.vo.Repo

@Dao
interface RepoDao {
    @Transaction
    @Query("SELECT * FROM Repo")
    fun getAll(): DataSource.Factory<Int, Repo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts : List<Repo>)
}