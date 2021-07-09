package com.robertohuertas.endless.models
import com.google.gson.annotations.SerializedName

data class StateResponse(
    @SerializedName("states")
    val states: List<State>?,
    @SerializedName("ttl")
    val ttl: Int?
)