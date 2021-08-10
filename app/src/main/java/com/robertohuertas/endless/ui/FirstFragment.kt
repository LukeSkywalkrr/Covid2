package com.robertohuertas.endless.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.robertohuertas.endless.Actions
import com.robertohuertas.endless.MainActivity
import com.robertohuertas.endless.R
import com.robertohuertas.endless.databinding.ActivityFirstBinding
import com.robertohuertas.endless.viewmodel.MyViewmodel
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch
import java.util.*

class FirstFragment:Fragment() {

    private val model: MyViewmodel by activityViewModels()
    lateinit var binding:ActivityFirstBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityFirstBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity
        var flag = activity.sharedPreferences.getBoolean("notify_button",true)


     //  binding.pinTextField.setText(activity.sharedPreferences.getString("s_PIN","").toString())


        //Notify Me Function
        binding.notify.setOnClickListener {
                model.demopin = binding.pinTextField.text.toString()
                Log.i("IS_flag",flag.toString())
            if(flag)
            {
                activity.myactionOnService(Actions.START)
                flag=false
            }else
            {
                activity.myactionOnService(Actions.STOP)
                flag=true
            }
            activity.sharedPreferences.edit().putBoolean("notify_button",flag).apply()
            Log.i("IS_flag2",flag.toString())
        }

        binding.checkAvailability.setOnClickListener {
            model.listOfButtons = mutableListOf(0,0,0,0,0,0,0,0,0)
            model.getCov(binding.pinTextField.text.toString(),model.currentDate, districtId = model.districtId)
           // activity.actionOnService()
           // actionOnService(Actions.STOP)
            model.demopin = binding.pinTextField.text.toString()
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)

           // Log.i("IS_",currentDate)
        }



        // RADIO BUTTON CLICK FUNCTIONALITY
        binding.radioEighteen.setOnClickListener {
            binding.radioFortify.isChecked = false
            model.iseighteen=true
        }

        binding.radioFortify.setOnClickListener {
            binding.radioEighteen.isChecked = false
            model.iseighteen =false

        }

        binding.radioSecond.setOnClickListener {
            binding.radioFirst.isChecked = false
            model.isFirstDose = false
        }

        binding.radioFirst.setOnClickListener {
               Log.i("Eighteen ","Effkjsnf")
            binding.radioSecond.isChecked = false
            model.isFirstDose=true
        }
        //handling spinners

        var statesArray:ArrayList<String>? = null

        lifecycleScope.launch {
            Log.d("sanchit", "onViewCreated: inside launch")
            statesArray = model.getStatesArray()
            Log.d("sanchit", "onViewCreated states array ${statesArray}")
            val adapter = statesArray?.let {
                ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    it
                )
            }
            binding.dropdownStates.adapter = adapter
        }

        var districtArray:ArrayList<String>? = null
        var stateId :Int = 0
        binding.dropdownStates.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val stateInString = parent?.getItemAtPosition(position)
                model.states?.forEach {
                    if(stateInString == it.stateName) {
                        stateId = it.stateId
                        Log.d("sanchit", "onItemSelected: $stateId")
                    }
                }

                lifecycleScope.launch {
                    Log.d("sanchit", "onItemSelected: ${model.getDistrict(stateId)}")
                    Log.d("sanchit", "onViewCreated: inside launch")
                    districtArray = model.getDistrictArray(stateId)
                    Log.d("sanchit", "onViewCreated states array ${districtArray}")
                    val adapter = districtArray?.let {
                        ArrayAdapter(
                            requireContext(),
                            R.layout.support_simple_spinner_dropdown_item,
                            it
                        )
                    }
                    binding.dropdownDistrict.adapter = adapter
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }


        binding.dropdownDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val districtInString = parent?.getItemAtPosition(position)
                model.district?.forEach {
                    if(districtInString == it.districtName) {
                        model.districtId = it.districtId!!
                        Log.d("sanchit", "onItemSelected: $stateId")
                    }
                }
                }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
            }
        }

        binding.btnPincode.setOnClickListener {
            model.districtId = 0
            binding.districtLayout.isGone = true
            binding.pincodeLayout.isVisible = true
        }

        binding.btnDistrict.setOnClickListener {
            binding.pinTextField.setText("")
            model.demopin=" "
            binding.districtLayout.isVisible = true
            binding.pincodeLayout.isGone = true
        }

    }
}