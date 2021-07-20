package com.valera.githubsearchusermvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.valera.githubsearchusermvvm.dao.DownloadDao
import com.valera.githubsearchusermvvm.model.Downloads

@Database(entities = [Downloads::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun downloadsDao(): DownloadDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "downloads.db"
            ).build()
    }
}