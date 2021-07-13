package com.robertohuertas.endless.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.robertohuertas.endless.R
import com.robertohuertas.endless.databinding.VaccineInfoItemBinding
import com.robertohuertas.endless.models.Covid
import com.robertohuertas.endless.models.Session

class RecyclerViewAdaptor(val covid: Covid) : RecyclerView.Adapter<RecyclerViewAdaptor.SheduleViewHolder>(){

    inner class SheduleViewHolder(private val binding: VaccineInfoItemBinding) : RecyclerView.ViewHolder(binding.root)
    {


        fun bind(session: Session?)
        {
                binding.hospitalName.text = session?.name
                binding.hospitalPincode.text =session?.center_id.toString()
                binding.ageGroup.text =session?.min_age_limit.toString()
                binding.fees.text = session?.fee
                binding.typeOfFees.text=session?.fee_type
                binding.date.text=session?.date
                binding.numberOfSlots.text = session?.available_capacity.toString()

                binding.firstSlot.text=session?.available_capacity_dose1.toString()


        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheduleViewHolder {    val view =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.vaccine_info_item, parent, false)
        val binding = VaccineInfoItemBinding.bind(view)


        return SheduleViewHolder(binding)


    }

    override fun onBindViewHolder(holder: SheduleViewHolder, position: Int) {
            val sesIem = covid.sessions[position]
            holder.bind(sesIem)
    }

    override fun getItemCount(): Int {

            return covid.sessions.size
    }


}