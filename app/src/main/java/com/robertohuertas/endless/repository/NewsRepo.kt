package com.robertohuertas.endless.repository

import com.robertohuertas.endless.api.RetrofitInstance

class NewsRepo {
    val covidApi = RetrofitInstance.api
    suspend fun getByDistrict(districtId : String,date :String)=  covidApi.getcov(districtId,date)
    suspend fun getbyPin(pincode : String,date :String)=covidApi.getbyPIN(pincode,date)
    suspend fun getStates() = covidApi.getStates()

    suspend fun getDistricts(stateId:Int)= covidApi.getDistricts(stateId.toString())
}