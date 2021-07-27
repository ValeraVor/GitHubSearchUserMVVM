package com.valera.githubsearchusermvvm.repositories

import com.valera.githubsearchusermvvm.api.DownloadService
import com.valera.githubsearchusermvvm.db.AppDataBase
import com.valera.githubsearchusermvvm.model.Downloads

class DownloadsRepository(private val db: AppDataBase,private val ds: DownloadService) {

    suspend fun insert(download : Downloads) = db.downloadsDao().insert(download)
    suspend fun getDownloads() = db.downloadsDao().getDownloads()

    fun downloadFile(login:String, nameRepository: String) {
        ds.provideDownload(login, nameRepository)
    }

}