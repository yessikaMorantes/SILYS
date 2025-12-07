package com.silys.create_request

import android.content.Intent
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.silys.R
import com.silys.home.Home
import com.silys.services.APIService
import com.silys.utils.TokenManager
import com.silys.utils.UIUtils
import com.silys.utils.UIUtils.Companion.hideLoading
import com.silys.utils.UIUtils.Companion.showLoading
import com.silys.utils.UIUtils.Companion.showSnackBar
import com.silys.utils.Utils
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class CreateRequest : AppCompatActivity() {
    lateinit var spinnerLabs: Spinner;
    lateinit var spinnerTopics: Spinner;
    lateinit var implementsList: MutableList<Spinner>;
    lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_request)
        val title = findViewById<TextView>(R.id.title)
        val subtitle = findViewById<TextView>(R.id.sub_title)
        val userName = findViewById<TextView>(R.id.user_name)
        val userCode = findViewById<TextView>(R.id.user_code)
        val prefixUserName = findViewById<TextView>(R.id.prefix_user_name)
        val buttonSend = findViewById<MaterialButton>(R.id.btn_add_request)
        rootView = findViewById(android.R.id.content)
        buttonSend.setOnClickListener { sendRequest() }
        setToolbar()
        lifecycleScope.launch {
            rootView.showLoading()
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
            rootView.hideLoading()
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
    fun sendRequest() {
        val name = findViewById<TextInputEditText>(R.id.name_project).text
        val description = findViewById<TextInputEditText>(R.id.description_project).text

        val laboratory = spinnerLabs.selectedItem;
        val topic = spinnerTopics.selectedItem;
        val listImplements = JSONArray();
        implementsList.toMutableList().forEach { implementsList ->
            listImplements.put(implementsList.selectedItem.toString())
        }

        if (name.toString().isEmpty() || description.toString().isEmpty() || listImplements.length() == 0) {
            rootView.showSnackBar("Todos los campos son obligatorios")
            return;
        }

        val json = JSONObject().apply {
            put("name", name)
            put("description", description)
            put("laboratory", laboratory)
            put("topic", topic)
            put("listImplements", listImplements)
        }
        lifecycleScope.launch {
            rootView.showLoading()
            val response = APIService().postJson("android/create-request", json, this@CreateRequest)
            if (response.optString("accessToken").isNullOrEmpty()
                || response.optString("refreshToken").isNullOrEmpty()
            ) {
                rootView.showSnackBar(response.optString("message"))
            } else {
                val intent = Intent(this@CreateRequest, Home::class.java)
                intent.putExtra("message", response.optString("message"))
                startActivity(intent)
                this@CreateRequest.finish()
            }
            rootView.hideLoading()
        }
    }

}