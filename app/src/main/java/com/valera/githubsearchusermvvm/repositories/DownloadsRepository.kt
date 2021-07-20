package com.valera.githubsearchusermvvm.repositories

import com.valera.githubsearchusermvvm.db.AppDataBase
import com.valera.githubsearchusermvvm.model.Downloads

class DownloadsRepository(private val db: AppDataBase) {

    suspend fun insert(download : Downloads) = db.downloadsDao().insert(download)
    suspend fun getDownloads() = db.downloadsDao().getDownloads()

}