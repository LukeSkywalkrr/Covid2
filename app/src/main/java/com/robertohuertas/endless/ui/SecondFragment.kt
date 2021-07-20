package com.robertohuertas.endless.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.robertohuertas.endless.R
import com.robertohuertas.endless.adaptor.RecyclerViewAdaptor
import com.robertohuertas.endless.databinding.ActivitySecondBinding
import com.robertohuertas.endless.viewmodel.MyViewmodel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myViewModel.distric.observe(viewLifecycleOwner, Observer {
            Log.d("newDistrict", "onViewCreated: $it")
        })
        myViewModel.covidFinalListForRV.observe(viewLifecycleOwner, Observer {
            Log.d("IS_", "Inside 2nd Fragment: $it")
            adapter = RecyclerViewAdaptor(
                it, context
            ) { findNavController().navigate(R.id.action_secondFragment_to_webViewFragment) }
            binding.recyclerView.adapter = adapter
        })

     //   binding.recyclerView.layoutManager = LinearLayoutManager(this


        binding.button1.setOnClickListener {
            myViewModel.buttonClicked(1)
            myViewModel.buttonUnselected(2)
            myViewModel.checkForFiltersToBeAddedToList()
            adapter?.notifyDataSetChanged()
        }
        binding.button2.setOnClickListener {
            myViewModel.buttonClicked(2)
            myViewModel.buttonUnselected(1)
            myViewModel.checkForFiltersToBeAddedToList()
            adapter?.notifyDataSetChanged()
        }
        binding.button3.setOnClickListener {
            myViewModel.buttonClicked(3)
            myViewModel.buttonUnselected(4)
            myViewModel.checkForFiltersToBeAddedToList()
            adapter?.notifyDataSetChanged()
        }
        binding.button4.setOnClickListener {
            myViewModel.buttonClicked(4)
            myViewModel.buttonUnselected(3)
            myViewModel.checkForFiltersToBeAddedToList()
            adapter?.notifyDataSetChanged()
        }

        //val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd"))

        val day1 = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val day2 = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val day3 = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        //val day1 = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

        // Click listeners for date changing buttons
        binding.button6.setOnClickListener {
            myViewModel.getCov(myViewModel.demopin, myViewModel.currentDate)
            Log.i("IS_", myViewModel.currentDate)
        }
        binding.button7.setOnClickListener {
            myViewModel.getCov(myViewModel.demopin, day1)
            Log.i("IS_", day1)
        }
        binding.button8.setOnClickListener {
            myViewModel.getCov(myViewModel.demopin, day2)
            Log.i("IS_", day2)
        }
        binding.button9.setOnClickListener {
            myViewModel.getCov(myViewModel.demopin, day3)
            Log.i("IS_", day3)
        }
    }
}