package com.labstock.create_request

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.labstock.R
import com.labstock.UiUtils

class CreateRequest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_request)
        setDropDownMenu()
        addFieldForImplement()
        setToolbar()
    }

    fun setDropDownMenu() {
        val actvLabs = findViewById<AutoCompleteTextView>(R.id.actv_labs_list)
        val items = resources.getStringArray(R.array.labs)
        val adapter = ArrayAdapter(this, R.layout.item_list_labs, items)
        actvLabs.setAdapter(adapter)
    }


    fun addFieldForImplement() {
        val container = findViewById<LinearLayout>(R.id.ly_item_container)
        val addButton = findViewById<ImageButton>(R.id.btn_add_item)

        val editTextList = mutableListOf<EditText>()

        addButton.setOnClickListener {
            val row = layoutInflater.inflate(R.layout.item_implement, container, false)

            val editText = row.findViewById<EditText>(R.id.tv_add_implement)
            val btnDeleteImplement = row.findViewById<ImageButton>(R.id.btn_delete_implement)

            editTextList.add(editText)
            UiUtils.Companion.animateAddView(row)

            btnDeleteImplement.setOnClickListener {
                UiUtils.Companion.animateRemoveView(container, row, editTextList, editText)
            }
            container.addView(row)
        }
    }

    fun setToolbar() {
        val btnBack = findViewById<ImageButton>(R.id.btn_toolbar)
        btnBack.setImageResource(R.drawable.back_arrow)
        btnBack.setOnClickListener {
            finish()
        }
    }
}