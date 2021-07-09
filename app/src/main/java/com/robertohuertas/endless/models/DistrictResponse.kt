package com.robertohuertas.endless.models
import com.google.gson.annotations.SerializedName

data class DistrictResponse(
    @SerializedName("districts")
    val districts: List<District>?,
    @SerializedName("ttl")
    val ttl: Int?
)