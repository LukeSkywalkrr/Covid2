package com.robertohuertas.endless.models
import com.google.gson.annotations.SerializedName

data class StateResponse(
    val states: List<State>,
    val ttl: Int
)