package com.gianlucaparadise.githubbrowser.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gianlucaparadise.githubbrowser.MainApplication
import com.gianlucaparadise.githubbrowser.vo.Repo
import com.gianlucaparadise.githubbrowser.vo.User

@Database(entities = [Repo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao
}