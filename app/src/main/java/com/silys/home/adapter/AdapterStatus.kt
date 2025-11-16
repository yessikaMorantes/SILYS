package com.silys.home.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.silys.home.item.ItemStatus
import com.silys.R
import org.json.JSONObject

class AdapterStatus(private val items: List<JSONObject>) :
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
        holder.tvItemStatus.text = item.optString("state_name")
        holder.tvItemStatus.setTextColor(item.optString("status_text_color").toColorInt())
        drawable.setColor(item.optString("status_color").toColorInt())
    }

    override fun getItemCount(): Int = items.size
}