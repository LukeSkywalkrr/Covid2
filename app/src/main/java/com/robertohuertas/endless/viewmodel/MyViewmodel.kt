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
    var iseighteen = true
    var isFirstDose = true
    val distric = MutableLiveData<List<District>>()
    val covid = MutableLiveData<Covid>()
    val covidListFromFirstFragment = MutableLiveData<List<Session>>()
    val listOfButtons = mutableListOf(0,0,0,0,0,0,0,0,0)
    var covidFinalListForRV = MutableLiveData<List<Session>>()

    var covidListToBeModified = MutableLiveData<List<Session>>()
    var demopin = "141001"
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

      fun getCov(pin : String) {
          viewModelScope.launch {
              covid.value=repository.getbyPin(pin,"17-07-2021").body()
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


          }
      }

    fun buttonClicked(button:Int){
        button.let { listOfButtons[it] = 1 }
    }

    fun buttonUnselected(button:Int){
        button.let { listOfButtons[it] = 0 }
    }

//    fun checkForFiltersToBeAddedToList(){
//        covidListToBeModified = covidListFromFirstFragment
//        covidFinalListForRV = covidListToBeModified.let { finalList->
//            for (button in listOfButtons){
//                when(button){
//                    1->
//                }
//            }
//        }
//    }




}