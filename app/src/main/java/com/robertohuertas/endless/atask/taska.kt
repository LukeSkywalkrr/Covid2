package com.robertohuertas.endless.atask

import com.robertohuertas.endless.api.RetrofitInstance

class taska() {
     suspend fun getcov(district : String,date :String)=
        RetrofitInstance.api.getcov(district,date)

    suspend fun getStates() = RetrofitInstance.api.getStates()

    var dis = "512"
}
