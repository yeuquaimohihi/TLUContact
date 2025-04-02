package com.example.contact_kotlin

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CbnvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameTextView: TextView = itemView.findViewById(R.id.txt_name_cbnv)
    val positionTextView: TextView = itemView.findViewById(R.id.txt_chucvu_cbnv)
    val phoneTextView: TextView = itemView.findViewById(R.id.txt_sdt_cbnv)
    val emailTextView: TextView = itemView.findViewById(R.id.txt_email_cbnv)
    val donviTextView: TextView = itemView.findViewById(R.id.txt_donvi_cbnv)

    fun bind(cbnv: Cbnv) {
        nameTextView.text = cbnv.nameCbnv
        positionTextView.text = cbnv.positionCbnv
        phoneTextView.text = cbnv.phoneCbnv
        emailTextView.text = cbnv.emailCbnv
    }
}