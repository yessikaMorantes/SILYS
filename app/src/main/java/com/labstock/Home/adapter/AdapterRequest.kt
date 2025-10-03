package com.labstock.Home.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.labstock.Home.item.ItemRequest
import com.labstock.R

class AdapterRequest(private val items: List<ItemRequest>,
                     private val listener: OnRequestClickListener) :
    RecyclerView.Adapter<AdapterRequest.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProjectName: TextView = itemView.findViewById(R.id.tv_project_name)
        val dateRequest: TextView = itemView.findViewById(R.id.tv_date_request)
        val tvTopicsRequest: TextView = itemView.findViewById(R.id.tv_topic_request)
        val tvRequestId: TextView = itemView.findViewById(R.id.tv_request_id)
        val tvLabRequest: TextView = itemView.findViewById(R.id.tv_lab_request)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description_request)
        val imgLabRequest: ImageView = itemView.findViewById(R.id.img_lab_request)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_item_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_request, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.tvProjectName.text = item.projectName
        holder.dateRequest.text = item.date
        holder.tvTopicsRequest.text = item.topic
        holder.tvLabRequest.text = item.lab
        holder.tvDescription.text = item.description
        holder.tvRequestId.text = item.requestCode
        holder.imgLabRequest.setImageResource(item.img)

        //Status
        val drawable = holder.tvStatus.background as GradientDrawable
        holder.tvStatus.text = item.status.statusName
        holder.tvStatus.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                item.status.textColor
            )
        );
        drawable.setColor(ContextCompat.getColor(holder.itemView.context, item.status.bgColor))

        fun Int.toPx(): Int =
            (this * holder.itemView.context.resources.displayMetrics.density).toInt()

        val paddingTop = holder.tvStatus.paddingTop
        val paddingBottom = holder.tvStatus.paddingBottom
        holder.tvStatus.setPadding(3.toPx(), paddingTop, 3.toPx(), paddingBottom)

        holder.itemView.setOnClickListener { view -> listener.onRequestClick(item) }
    }

    override fun getItemCount(): Int = items.size
}