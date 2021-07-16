package com.robertohuertas.endless.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.robertohuertas.endless.adaptor.RecyclerViewAdaptor
import com.robertohuertas.endless.databinding.ActivitySecondBinding
import com.robertohuertas.endless.viewmodel.MyViewmodel

class SecondFragment: Fragment() {
    var  adapter:RecyclerViewAdaptor? = null
    private val myViewModel: MyViewmodel by activityViewModels()
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

        myViewModel.distric.observe(viewLifecycleOwner, Observer {
            Log.d("newDistrict", "onViewCreated: $it")
        })

        myViewModel.covidFinalListForRV.observe(viewLifecycleOwner, Observer {
            Log.d("XXX", "onViewCreated: $it")
            if(!it.isNullOrEmpty())
            {
                adapter = RecyclerViewAdaptor(it)
                binding.recyclerView.adapter = adapter
            }

        })

     //   binding.recyclerView.layoutManager = LinearLayoutManager(this


        binding.button1.setOnClickListener {
            myViewModel.buttonClicked(1)
            myViewModel.buttonUnselected(2)
            myViewModel.checkForFiltersToBeAddedToList()
        }
        binding.button2.setOnClickListener {
            myViewModel.buttonClicked(2)
            myViewModel.buttonUnselected(1)
            myViewModel.checkForFiltersToBeAddedToList()
        }
        binding.button3.setOnClickListener {
            myViewModel.buttonClicked(3)
            myViewModel.buttonUnselected(4)
            myViewModel.checkForFiltersToBeAddedToList()
        }
        binding.button4.setOnClickListener {
            myViewModel.buttonClicked(4)
            myViewModel.buttonUnselected(3)
            myViewModel.checkForFiltersToBeAddedToList()
        }


    }
}