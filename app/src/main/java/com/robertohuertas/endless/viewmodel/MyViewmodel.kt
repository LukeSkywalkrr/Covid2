package com.robertohuertas.endless.viewmodel

import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertohuertas.endless.*
import com.robertohuertas.endless.api.RetrofitInstance
import com.robertohuertas.endless.models.District
import com.robertohuertas.endless.repository.NewsRepo
import kotlinx.coroutines.launch

class MyViewmodel : ViewModel() {
    val repository = NewsRepo()

    val distric = MutableLiveData<List<District>>()

    fun getCovid(){
        viewModelScope.launch {
            distric.value = repository.getDistricts().body()?.districts!!
        }
    }



}