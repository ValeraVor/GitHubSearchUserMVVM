package com.valera.githubsearchusermvvm.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User (
    @SerializedName("id")
    val id : String,
    @SerializedName("login")
    val login : String,
    @SerializedName("avatar_url")
    val avatar_url : String
) : Serializable