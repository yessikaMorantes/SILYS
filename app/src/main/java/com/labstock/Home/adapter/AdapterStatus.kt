package com.labstock.Home.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.labstock.Home.item.ItemStatus
import com.labstock.R

class AdapterStatus(private val items: List<ItemStatus>) :
    RecyclerView.Adapter<AdapterStatus.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemStatus: TextView = itemView.findViewById(R.id.tv_item_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_status, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        val drawable = holder.tvItemStatus.background as GradientDrawable
        holder.tvItemStatus.text = item.statusName
        holder.tvItemStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, item.textColor))
        drawable.setColor(ContextCompat.getColor(holder.itemView.context, item.bgColor))
    }

    override fun getItemCount(): Int = items.size
}