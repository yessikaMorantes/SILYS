package com.silys.show_request

import android.os.Bundle
import android.text.Html
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.silys.home.adapter.AdapterCheck
import com.silys.home.item.ItemRequest
import com.silys.R

class ShowRequest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_request)
        setToolbar()
        loadViewImplementList()
        val item = intent.getParcelableExtra<ItemRequest>("item_request")
        item?.let {
            loadViewFromItem(it)
        }
    }
    fun loadViewFromItem(item : ItemRequest){
        findViewById<TextView>(R.id.tv_project_name).text = item.projectName
        findViewById<TextView>(R.id.tv_settled_request).text = item.requestCode
        findViewById<TextView>(R.id.tv_request_id).text = item.requestCode
        //Dummy text: This will be returned from the backend
        findViewById<TextView>(R.id.tv_date_request).text = Html.fromHtml("<b>Fecha de solicitud: </b>" + item.date)
        findViewById<TextView>(R.id.tv_date_approved).text = Html.fromHtml("<b>Fecha de aprobacion: </b>" + item.date)
        findViewById<TextView>(R.id.tv_topic_request).text = Html.fromHtml("<b>Materia: </b>" + item.topic)
        findViewById<TextView>(R.id.tv_laboratory_request).text = Html.fromHtml("<b>Laboratorio: </b>" + item.lab)
        findViewById<TextView>(R.id.tv_description_request).text = Html.fromHtml("<b>Motivo: </b>" + item.description)
    }

    fun loadViewImplementList(){
        val implementsList: List<String> = listOf("Arduino", "Panel solar", "Jumpers")

        val rvImplements = findViewById<RecyclerView>(R.id.rv_check)
        rvImplements.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        rvImplements.adapter = AdapterCheck(implementsList)
    }

    fun setToolbar() {
        val btnBack = findViewById<ImageButton>(R.id.btn_toolbar)
        btnBack.setImageResource(R.drawable.back_arrow)
        btnBack.setOnClickListener {
            finish()
        }
    }
}