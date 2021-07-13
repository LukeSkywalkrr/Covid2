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

        binding.notify.setOnClickListener {
            val activity = activity as MainActivity
            activity.actionOnService(Actions.START)
        }
        binding.checkAvailability.setOnClickListener {
            model.getCov()
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }
    }
}