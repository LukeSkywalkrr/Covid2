package com.robertohuertas.endless.repository

import com.robertohuertas.endless.api.RetrofitInstance

class NewsRepo {
    val covidApi = RetrofitInstance.api
    suspend fun getcov(district : String,date :String)=
        covidApi.getcov(district,date)

    suspend fun getStates() = covidApi.getStates()

    suspend fun getDistricts()= covidApi.getDistricts()
}