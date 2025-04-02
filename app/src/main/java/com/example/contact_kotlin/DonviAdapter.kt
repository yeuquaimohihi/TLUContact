package com.example.contact_kotlin

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class DonviAdapter(private var donvis: Array<Donvi>) : RecyclerView.Adapter<DonviAdapter.DonviViewHolder>() {

    class DonviViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.txt_donvi_name)
        val phoneTextView: TextView = itemView.findViewById(R.id.txt_donvi_sdt)
        val addressTextView: TextView = itemView.findViewById(R.id.txt_donvi_diachi)

        fun bind(donvi: Donvi) {
            nameTextView.text = donvi.name
            phoneTextView.text = donvi.phone
            addressTextView.text = donvi.address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonviViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_donvi, parent, false)
        return DonviViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DonviViewHolder, position: Int) {
        holder.bind(donvis[position])

        // Set click listener for the CardView
        holder.itemView.setOnClickListener {
            showDonviDetailsDialog(donvis[position], holder)
        }
    }

    override fun getItemCount(): Int = donvis.size

    private fun showDonviDetailsDialog(donvi: Donvi, holder: DonviViewHolder) {
        val context = holder.itemView.context
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_donvi_details, null)

        // Set up the dialog views
        val nameTextView = dialogView.findViewById<TextView>(R.id.tv_dialog_name)
        val phoneTextView = dialogView.findViewById<TextView>(R.id.tv_dialog_phone)
        val addressTextView = dialogView.findViewById<TextView>(R.id.tv_dialog_address)
        val callButton = dialogView.findViewById<Button>(R.id.btn_call)

        // Set the data
        nameTextView.text = "Tên đơn vị: ${donvi.name}"
        phoneTextView.text = "Số điện thoại: ${donvi.phone}"
        addressTextView.text = "Địa chỉ: ${donvi.address}"

        // Set up the call button
        callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${donvi.phone}")
            }
            context.startActivity(intent)
        }

        builder.setView(dialogView)
        builder.setNegativeButton("Đóng") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
    fun updateData(newDonvis: Array<Donvi>) {
        this.donvis = newDonvis
        notifyDataSetChanged()
    }
}