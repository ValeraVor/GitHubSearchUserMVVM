package com.valera.githubsearchusermvvm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Downloads (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "login")
    val login: String,
    @ColumnInfo(name = "nameRepository")
    val nameRepository: String
)
