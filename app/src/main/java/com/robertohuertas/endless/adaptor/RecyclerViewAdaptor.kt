package com.robertohuertas.endless.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robertohuertas.endless.R
import com.robertohuertas.endless.databinding.VaccineInfoItemBinding
import com.robertohuertas.endless.models.Session

class RecyclerViewAdaptor(
    private val covid: List<Session>?,
    private val context: Context?,
    val clickOfBookNow: () -> Unit
    ) :
    RecyclerView.Adapter<RecyclerViewAdaptor.SheduleViewHolder>(){

    inner class SheduleViewHolder(private val binding: VaccineInfoItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(session: Session?)
        {
            binding.hospitalName.text = session?.name
            binding.hospitalPincode.text =session?.pincode.toString()
            binding.ageGroup.text =session?.min_age_limit.toString()
            binding.fees.text = session?.fee
            binding.typeOfFees.text=session?.fee_type
            binding.date.text=session?.date
            binding.vaccineName.text=session?.vaccine.toString()
            binding.firstDose.text =
                context?.getString(R.string.dose_1)?.let { String.format(it, session?.available_capacity_dose1) }
            binding.secondDose.text =
                context?.getString(R.string.dose_2)?.let { String.format(it, session?.available_capacity_dose2) }
            binding.numberOfSlots.text =
                context?.getString(R.string.slots)?.let { String.format(it, session?.available_capacity) }

            binding.bookNow.setOnClickListener { clickOfBookNow.invoke() }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheduleViewHolder {    val view =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.vaccine_info_item, parent, false)
        val binding = VaccineInfoItemBinding.bind(view)
        return SheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SheduleViewHolder, position: Int) {
        val  sesIem = covid?.get(position)
                holder.bind(sesIem)
    }

    override fun getItemCount(): Int {

        return covid?.size ?: 0
    }


}