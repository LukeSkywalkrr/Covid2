package com.robertohuertas.endless.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertohuertas.endless.models.Covid
import com.robertohuertas.endless.models.District
import com.robertohuertas.endless.models.Session
import com.robertohuertas.endless.repository.NewsRepo
import kotlinx.coroutines.launch

class MyViewmodel : ViewModel() {
    val repository = NewsRepo()

    val distric = MutableLiveData<List<District>>()
    val covid = MutableLiveData<Covid>()
    val covidListFromFirstFragment = MutableLiveData<List<Session>>()
    val listOfButtons = mutableListOf(0,0,0,0,0,0,0,0,0)
    var covidFinalListForRV = MutableLiveData<List<Session>>()

    var covidListToBeModified = MutableLiveData<List<Session>>()

    init {
        covidListToBeModified = covidListFromFirstFragment
    }


    fun getDistrict() {
        viewModelScope.launch {
            distric.value = repository.getDistricts().body()?.districts!!
        }
    }
    fun revUpdate() {
        covidListFromFirstFragment.value = covid.value?.sessions?.filterNot { it.available_capacity == 1 }
    }

      fun getCov() {
          viewModelScope.launch {
              covid.value=repository.getcov("512","17-07-2021").body()
              covidListFromFirstFragment.value = covid.value?.sessions
              Log.i("GetCOV",covid.value.toString())
              covidListFromFirstFragment.value = covidListFromFirstFragment.value?.filter { it.available_capacity == 1 }
          }
      }

    fun buttonClicked(button:Int){
        button.let { listOfButtons[it] = 1 }
    }

    fun buttonUnselected(button:Int){
        button.let { listOfButtons[it] = 0 }
    }

    fun checkForFiltersToBeAddedToList(){
        covidListToBeModified = covidListFromFirstFragment
        covidFinalListForRV = covidListToBeModified.let { finalList->
            for (button in listOfButtons){
                when(button){
                    1->
                }
            }
        }
    }



}