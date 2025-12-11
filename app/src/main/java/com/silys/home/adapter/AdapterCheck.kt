package com.silys.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.silys.R

class AdapterCheck(
    private val items: List<String>,
    private val isCheckBox: Boolean
) : RecyclerView.Adapter<AdapterCheck.MyViewHolder>() {

    private val selectedItems = mutableListOf<String>()

    class MyViewHolder(itemView: View, isCheckBox: Boolean) : RecyclerView.ViewHolder(itemView) {
        var checkImplements: CheckBox? = null
        var itemText: TextView? = null

        init {
            if (isCheckBox) {
                checkImplements = itemView.findViewById(R.id.check_implements)
            } else {
                itemText = itemView.findViewById(R.id.tv_item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            if (isCheckBox) R.layout.item_recycler_check
            else R.layout.item_implement_waiting,
            parent,
            false
        )
        return MyViewHolder(view, isCheckBox)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]

        holder.checkImplements?.text = item
        holder.itemText?.text = item

        if (isCheckBox) {
            val cb = holder.checkImplements!!

            cb.setOnCheckedChangeListener(null)
            cb.isChecked = selectedItems.contains(item)

            cb.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!selectedItems.contains(item)) {
                        selectedItems.add(item)
                    }
                } else {
                    selectedItems.remove(item)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun getSelectedItems(): List<String> = selectedItems
}
