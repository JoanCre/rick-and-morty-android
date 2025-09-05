package com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto

import com.google.gson.annotations.SerializedName

data class PageDto(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?
)
