package com.example.contact_kotlin

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class DonviViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val namedvTextView: TextView = itemView.findViewById(R.id.txt_donvi_name)
    private val phoneTextView: TextView = itemView.findViewById(R.id.txt_donvi_sdt)
    private val addressTextView: TextView = itemView.findViewById(R.id.txt_donvi_diachi)
    val cardView: CardView = itemView.findViewById(R.id.cv_donvi)

    fun bind(donvi: Donvi) {
        namedvTextView.text = donvi.name
        phoneTextView.text = donvi.phone
        addressTextView.text = donvi.address
    }
}