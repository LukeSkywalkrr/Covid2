package com.robertohuertas.endless.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertohuertas.endless.models.Covid
import com.robertohuertas.endless.models.District
import com.robertohuertas.endless.models.Session
import com.robertohuertas.endless.models.State
import com.robertohuertas.endless.repository.NewsRepo
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyViewmodel : ViewModel() {
    val repository = NewsRepo()
    var iseighteen = true
    var isFirstDose = true
    val distric = MutableLiveData<List<District>>()
    val sdf = SimpleDateFormat("dd-M-yyyy")
    val currentDate = sdf.format(Date())
    private var covid:Covid?= null
    private val covidListFromFirstFragment = MutableLiveData<List<Session>>()
    var listOfButtons = mutableListOf(0,0,0,0,0,0,0,0,0)
    var covidFinalListForRV = MutableLiveData<List<Session>>()

    var covidListToBeModified = MutableLiveData<List<Session>>()
    var demopin = "221001"
    var districtId:Int = 0

    suspend fun getDistrict(stateId:Int): List<District> {
            return repository.getDistricts(stateId).body()?.districts!!
    }

    var district:List<District>? = null
    suspend fun getDistrictArray(stateId: Int):ArrayList<String>{
            Log.d("IX_", "getStatesArray: inside ")
            val names = ArrayList<String>()
//        Log.d("sanchit", "getStatesArray: ${repository.getStates().body()?.states}")
            district = try {
                Log.d("sanchit", "getStatesArray: inside try")
                repository.getDistricts(stateId).body()?.districts
            } catch (e:Exception){
                Log.d("sanchit", "getStatesArray: exceptioh: ${e}")
                null}

            Log.d("sanchit", "getStatesArray:inside viewmodel states =  $states")
            district?.forEach {
                names.add(it.districtName!!)
            }
            return names
    }
    fun revUpdate() {
        covidListFromFirstFragment.value = covid?.sessions?.filterNot { it.available_capacity == 1 }
    }

      fun getCov(pin : String,date: String, districtId:Int) {
          viewModelScope.launch {
              Log.d("IQ_", "getCov: district id = $districtId, pin = $pin,demopi =$demopin")
              if(pin.isNotBlank())
              covid=repository.getbyPin(pin,date).body()
              Log.d("sanchit", "getCov: by pincode ${repository.getbyPin(pin,date).body()}")

              if(districtId!=0)
              covid = repository.getByDistrict( districtId.toString(), date).body()

              Log.d("sanchit", "getCov: by district ${ repository.getByDistrict( districtId.toString(), date).body()}")
              covidListFromFirstFragment.value = covid?.sessions
              Log.i("GetCOV",covid.toString())
             // covidListFromFirstFragment.value = covidListFromFirstFragment.value?.filter { it.available_capacity == 1 }
              //LOGIC TO FILTER for firstFragment
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

    var states:List<State>? = null
    suspend fun getStatesArray(): ArrayList<String> {
        Log.d("sanchit", "getStatesArray: inside ")
        val names = ArrayList<String>()
//        Log.d("sanchit", "getStatesArray: ${repository.getStates().body()?.states}")
        states = try {
            Log.d("sanchit", "getStatesArray: inside try")
            repository.getStates().body()?.states
        } catch (e:Exception){
            Log.d("sanchit", "getStatesArray: exceptioh: ${e}")
            null}

        Log.d("sanchit", "getStatesArray:inside viewmodel states =  $states")
        states?.forEach {
            names.add(it.stateName)
        }
        return names
    }

}