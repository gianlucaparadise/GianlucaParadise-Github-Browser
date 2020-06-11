package com.gianlucaparadise.githubbrowser.db

import androidx.paging.DataSource
import androidx.room.*
import com.gianlucaparadise.githubbrowser.vo.Repo

@Dao
interface RepoDao {
    @Transaction
    @Query("SELECT * FROM Repo")
    fun getAll(): DataSource.Factory<Int, Repo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts : List<Repo>)

    @Update
    suspend fun update(repo: Repo)
}