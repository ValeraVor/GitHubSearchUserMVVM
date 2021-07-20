package com.valera.githubsearchusermvvm.model

import com.google.gson.annotations.SerializedName

data class RepositoriesProfile(
    @SerializedName("id") val id : String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("html_url") val html_url: String,
    @SerializedName("language") val language: String
)