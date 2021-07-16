package com.robertohuertas.endless.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.robertohuertas.endless.*
import com.robertohuertas.endless.databinding.ActivityFirstBinding
import com.robertohuertas.endless.viewmodel.MyViewmodel

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
        var flag = true
        binding.notify.setOnClickListener {

            if(flag)
            {
                activity.actionOnService(Actions.START)
                flag=false
            }else
            {
                activity.actionOnService(Actions.STOP)
                flag=true
            }

        }

        binding.checkAvailability.setOnClickListener {
            model.getCov(binding.pinTextField.text.toString())
           // activity.actionOnService()
           // actionOnService(Actions.STOP)
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
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
    }
}