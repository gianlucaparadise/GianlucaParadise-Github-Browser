package com.gianlucaparadise.githubbrowser.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gianlucaparadise.githubbrowser.MainApplication

@Database(entities = [User::class, Repo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun repoDao(): RepoDao

    companion object {

        val instance: AppDatabase by lazy {
            Room.databaseBuilder(
                MainApplication.applicationContext,
                AppDatabase::class.java, "gituhub-data"
            ).build()
        }
    }
}