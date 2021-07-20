package com.valera.githubsearchusermvvm.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.valera.githubsearchusermvvm.model.Downloads

@Dao
interface DownloadDao {

    @Insert
    fun insert(image: Downloads)

    @Query("SELECT * FROM Downloads")
    fun getDownloads(): MutableList<Downloads>

}