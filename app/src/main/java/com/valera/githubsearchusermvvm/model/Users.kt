package com.valera.githubsearchusermvvm.model

import com.google.gson.annotations.SerializedName

data class Users (
    @SerializedName("total_count")
    val total: Int,
    @SerializedName("items")
    val items: List<User>
)