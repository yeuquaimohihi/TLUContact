package com.example.contact_kotlin

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.Button

class CbnvAdapter(private var cbnvs: Array<Cbnv>) : RecyclerView.Adapter<CbnvAdapter.CbnvViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CbnvViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cbnv, parent, false)
        return CbnvViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CbnvViewHolder, position: Int) {
        val cbnv = cbnvs[position]
        holder.bind(cbnv)

        // Handle donvi name lookup separately
        val dbHelper = ContactDatabaseHelper()
        if (cbnv.donviId != 0) {
            dbHelper.getDonviById(cbnv.donviId) { donvi ->
                holder.donviTextView.text = donvi.name
            }
        } else {
            holder.donviTextView.text = "Chưa có đơn vị"
        }

        // Set click listener for the CardView
        holder.itemView.setOnClickListener {
            showCbnvDetailsDialog(cbnv, holder)
        }
    }

    override fun getItemCount(): Int = cbnvs.size

    private fun showCbnvDetailsDialog(cbnv: Cbnv, holder: CbnvViewHolder) {
        val context = holder.itemView.context
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_cbnv_details, null)

        // Set up the dialog views
        val nameTextView = dialogView.findViewById<TextView>(R.id.tv_dialog_name_cbnv)
        val positionTextView = dialogView.findViewById<TextView>(R.id.tv_dialog_chucvu)
        val phoneTextView = dialogView.findViewById<TextView>(R.id.tv_dialog_phone_cbnv)
        val emailTextView = dialogView.findViewById<TextView>(R.id.tv_dialog_email)
        val callButton = dialogView.findViewById<Button>(R.id.btn_call_cbnv)

        // Set the data
        nameTextView.text = "Họ tên: ${cbnv.nameCbnv}"
        positionTextView.text = "Chức vụ: ${cbnv.positionCbnv}"
        phoneTextView.text = "Số điện thoại: ${cbnv.phoneCbnv}"
        emailTextView.text = "Email: ${cbnv.emailCbnv}"

        // Set up the call button
        callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${cbnv.phoneCbnv}")
            }
            context.startActivity(intent)
        }

        builder.setView(dialogView)
        builder.setNegativeButton("Close") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    fun updateData(newCbnvs: Array<Cbnv>) {
        this.cbnvs = newCbnvs
        notifyDataSetChanged()
    }

}