package com.silys.create_request

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.silys.R
import com.silys.services.APIService
import com.silys.utils.UIUtils
import com.silys.utils.Utils
import kotlinx.coroutines.launch

class CreateRequest : AppCompatActivity() {
    lateinit var spinnerLabs: Spinner;
    lateinit var spinnerTopics: Spinner;
    lateinit var implementsList: MutableList<Spinner>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_request)
        val title = findViewById<TextView>(R.id.title)
        val subtitle = findViewById<TextView>(R.id.sub_title)
        val userName = findViewById<TextView>(R.id.user_name)
        val userCode = findViewById<TextView>(R.id.user_code)
        val prefixUserName = findViewById<TextView>(R.id.prefix_user_name)
        setToolbar()
        lifecycleScope.launch {
            val response = APIService().getJson("android/create-request", this@CreateRequest)
            try {
                title.text = response.optString("title")
                subtitle.text = response.optString("sub_title")
                userName.text = response.optString("user_name")
                userCode.text = response.optString("user_code")
                prefixUserName.text = response.optString("prefix_user_name")


                setSpinnerLaboratories(Utils().convertJSONArrayToList(response.getJSONArray("listLaboratories")))
                setSpinnerTopics(Utils().convertJSONArrayToList(response.getJSONArray("listTopics")))
                addFieldForImplement(Utils().convertJSONArrayToList(response.getJSONArray("listItems")))
            } catch (e: Exception) {
                var message: String = response.optString("message");
                if (message.isBlank()) {
                    message = response.optString("error");
                }
                e.printStackTrace()
                Toast.makeText(this@CreateRequest, message, Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    this@CreateRequest.finish()
                }, 2000)
            }
        }
    }

    fun setSpinnerLaboratories(list: List<String>) {
        spinnerLabs = findViewById(R.id.spinner_laboratories)
        if (list.isEmpty()) {
            spinnerLabs.visibility = View.GONE
            return;
        }
        val adapter = ArrayAdapter(this, R.layout.tv_spinner, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLabs.adapter = adapter
    }

    fun setSpinnerTopics(list: List<String>) {
        spinnerTopics = findViewById(R.id.spinner_topics)
        if (list.isEmpty()) {
            spinnerTopics.visibility = View.GONE
            return;
        }
        val adapter = ArrayAdapter(this, R.layout.tv_spinner, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTopics.adapter = adapter
    }


    fun addFieldForImplement(list: List<String>) {
        val container = findViewById<LinearLayout>(R.id.ly_item_container)
        val addButton = findViewById<ImageButton>(R.id.btn_add_item)

        val adapter = ArrayAdapter(this, R.layout.tv_spinner, list)

        if(list.isEmpty()){
            return;
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        implementsList = mutableListOf()

        addButton.setOnClickListener {
            val row = layoutInflater.inflate(R.layout.item_implement, container, false)

            val implements = row.findViewById<Spinner>(R.id.spinner)
            val btnDeleteImplement = row.findViewById<ImageButton>(R.id.btn_delete_implement)
            implements.adapter = adapter
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