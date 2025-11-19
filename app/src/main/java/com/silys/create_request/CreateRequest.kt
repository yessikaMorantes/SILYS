package com.silys.create_request

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.silys.R
import com.silys.utils.UIUtils

class CreateRequest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_request)
        setSpinnerLaboratories()
        setSpinnerTopics()
        addFieldForImplement()
        setToolbar()
    }

    fun setSpinnerLaboratories() {
        val spinnerLabs = findViewById<Spinner>(R.id.spinner_laboratories)
        val items = resources.getStringArray(R.array.labs)
        val adapter = ArrayAdapter(this, R.layout.tv_spinner, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLabs.adapter = adapter
        spinnerLabs.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                //Toast.makeText(this@CreateRequest, "Laboratorio: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }
    fun setSpinnerTopics() {
        val spinnerLabs = findViewById<Spinner>(R.id.spinner_topics)
        val items = resources.getStringArray(R.array.topics)
        val adapter = ArrayAdapter(this, R.layout.tv_spinner, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLabs.adapter = adapter
        spinnerLabs.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                //Toast.makeText(this@CreateRequest, "Materia: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }


    fun addFieldForImplement() {
        val container = findViewById<LinearLayout>(R.id.ly_item_container)
        val addButton = findViewById<ImageButton>(R.id.btn_add_item)

        val items = resources.getStringArray(R.array.implement_list)
        val adapter = ArrayAdapter(this, R.layout.tv_spinner, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val implementsList = mutableListOf<Spinner>()

        addButton.setOnClickListener {
            val row = layoutInflater.inflate(R.layout.item_implement, container, false)

            val implements = row.findViewById<Spinner>(R.id.spinner)
            val btnDeleteImplement = row.findViewById<ImageButton>(R.id.btn_delete_implement)
            implements.adapter = adapter
            implements.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

            implementsList.add(implements)
            UIUtils.Companion.animateAddView(row)

            btnDeleteImplement.setOnClickListener {
                UIUtils.Companion.animateRemoveView(container, row, implementsList, implements)
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