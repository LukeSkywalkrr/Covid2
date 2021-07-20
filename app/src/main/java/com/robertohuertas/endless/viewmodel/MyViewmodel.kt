package com.robertohuertas.endless.viewmodel

import android.content.Context
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
import java.text.SimpleDateFormat
import java.util.*

class MyViewmodel : ViewModel() {
    val repository = NewsRepo()
    var iseighteen = true
    var isFirstDose = true
    val distric = MutableLiveData<List<District>>()
    val sdf = SimpleDateFormat("dd-M-yyyy")
    val currentDate = sdf.format(Date())
    private val covid = MutableLiveData<Covid>()
    private val covidListFromFirstFragment = MutableLiveData<List<Session>>()
    var listOfButtons = mutableListOf(0,0,0,0,0,0,0,0,0)
    var covidFinalListForRV = MutableLiveData<List<Session>>()

    var covidListToBeModified = MutableLiveData<List<Session>>()
    var demopin = "141001"

    fun getDistrict() {
        viewModelScope.launch {
            distric.value = repository.getDistricts().body()?.districts!!
        }
    }
    fun revUpdate() {
        covidListFromFirstFragment.value = covid.value?.sessions?.filterNot { it.available_capacity == 1 }
    }

      fun getCov(pin : String,date: String) {
          viewModelScope.launch {
              covid.value=repository.getbyPin(pin,date).body()
              covidListFromFirstFragment.value = covid.value?.sessions
              Log.i("GetCOV",covid.value.toString())
             // covidListFromFirstFragment.value = covidListFromFirstFragment.value?.filter { it.available_capacity == 1 }
              //LOGIC TO FILTER
              if(iseighteen)
              {
                  covidListFromFirstFragment.value = covidListFromFirstFragment.value?.filter { it.min_age_limit == 18 }
              }else
              {
                  covidListFromFirstFragment.value = covidListFromFirstFragment.value?.filter { it.min_age_limit ==45   }
              }
              if(isFirstDose)
              {
                  covidListFromFirstFragment.value = covidListFromFirstFragment.value?.filter { it.available_capacity_dose1 > 0   }
              }else
              {
                  covidListFromFirstFragment.value = covidListFromFirstFragment.value?.filter { it.available_capacity_dose2 > 0   }
              }
              covidFinalListForRV.value = covidListFromFirstFragment.value
           //   listOfButtons = mutableListOf(0,0,0,0,0,0,0,0,0)
              checkForFiltersToBeAddedToList()
          }
      }

    fun buttonClicked(button:Int){
        button.let { listOfButtons[it] = 1 }
        Log.d("second", "buttonClicked: $listOfButtons")
    }

    fun buttonUnselected(button:Int){
        button.let { listOfButtons[it] = 0 }
        Log.d("second", "buttonClicked: $listOfButtons")

    }

    fun checkForFiltersToBeAddedToList(){
        covidListToBeModified.value = covidListFromFirstFragment.value
        val temporaryList = covidListToBeModified.value?.toMutableList()
        if(listOfButtons[1] == 1){temporaryList?.retainAll{it.fee_type == "Free"}}
        Log.d("second", "checkForFiltersToBeAddedToList: $temporaryList")

         if(listOfButtons[2] == 1){temporaryList?.retainAll{it.fee_type == "Paid"}}
        Log.d("second", "checkForFiltersToBeAddedToList: $temporaryList")

        if(listOfButtons[3] == 1){temporaryList?.retainAll{it.vaccine == "COVISHIELD"}}
        Log.d("second", "checkForFiltersToBeAddedToList: $temporaryList")

        if(listOfButtons[4] == 1){temporaryList?.retainAll{it.vaccine == "COVAXIN"}}
        Log.d("second", "checkForFiltersToBeAddedToList: $temporaryList")

        covidFinalListForRV.postValue(temporaryList)
    }




}