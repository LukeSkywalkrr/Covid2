package com.robertohuertas.endless.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.robertohuertas.endless.adaptor.RecyclerViewAdaptor
import com.robertohuertas.endless.databinding.ActivityFirstBinding
import com.robertohuertas.endless.databinding.ActivitySecondBinding
import com.robertohuertas.endless.viewmodel.MyViewmodel

class SecondFragment: Fragment() {
    var  adapter:RecyclerViewAdaptor? = null
    private val model: MyViewmodel by activityViewModels()
    lateinit var binding: ActivitySecondBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivitySecondBinding.inflate(inflater, container, false)
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.distric.observe(viewLifecycleOwner, Observer {
            Log.d("newDistrict", "onViewCreated: $it")
        })

        model.covid.observe(viewLifecycleOwner, Observer {
            Log.d("XXX", "onViewCreated: $it")
            adapter = RecyclerViewAdaptor(it)
            binding.recyclerView.adapter = adapter
        })

     //   binding.recyclerView.layoutManager = LinearLayoutManager(this





    }
}