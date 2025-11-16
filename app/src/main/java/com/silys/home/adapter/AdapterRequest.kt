package com.silys.home.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.silys.home.item.ItemRequest
import com.silys.R
import org.json.JSONObject

class AdapterRequest(private val items: List<JSONObject>,
                     private val listener: OnRequestClickListener,
    private val context: Context) :
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
        holder.tvProjectName.text = item.optString("name")
        holder.dateRequest.text = item.optString("date_request")
        holder.tvTopicsRequest.text = item.optString("topic_name")
        holder.tvLabRequest.text = item.optString("laboratory_name")
        holder.tvDescription.text = item.optString("description")
        holder.tvRequestId.text = item.optString("code")
        Glide.with(context)
            .load(item.optString("laboratory_img"))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into( holder.imgLabRequest)

        //Status
        val drawable = holder.tvStatus.background as GradientDrawable
        holder.tvStatus.text = item.optString("state_name")
        holder.tvStatus.setTextColor(item.optString("status_text_color").toColorInt());
        drawable.setColor(item.optString("status_color").toColorInt())

        fun Int.toPx(): Int =
            (this * holder.itemView.context.resources.displayMetrics.density).toInt()

        val paddingTop = holder.tvStatus.paddingTop
        val paddingBottom = holder.tvStatus.paddingBottom
        holder.tvStatus.setPadding(3.toPx(), paddingTop, 3.toPx(), paddingBottom)

        holder.itemView.setOnClickListener { view -> listener.onRequestClick(item) }
    }

    override fun getItemCount(): Int = items.size
}