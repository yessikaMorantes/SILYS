package com.labstock.Home

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.labstock.Home.adapter.AdapterRequest
import com.labstock.Home.adapter.AdapterStatus
import com.labstock.Home.adapter.OnRequestClickListener
import com.labstock.Home.item.ItemRequest
import com.labstock.Home.item.ItemStatus
import com.labstock.create_request.CreateRequest
import com.labstock.R
import com.labstock.show_request.ShowRequest

class Home : AppCompatActivity(), OnRequestClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        buttons()

        val rvStatus = findViewById<RecyclerView>(R.id.rv_status)
        val rvRequest = findViewById<RecyclerView>(R.id.rv_requests)

        val statusAll = ItemStatus("Todo", R.color.all_bg_color, R.color.all_text_color)
        val statusApproved =
            ItemStatus("Aprobado", R.color.approved_bg_color, R.color.approved_text_color)
        val statusPending =
            ItemStatus("Pendiente", R.color.pending_bg_color, R.color.pending_text_color)
        val statusLended =
            ItemStatus("Prestado", R.color.lended_bg_color, R.color.lended_text_color)
        val statusReturned =
            ItemStatus("Devuelto", R.color.returned_bg_color, R.color.returned_text_color)
        val statusDeclined =
            ItemStatus("Rechazado", R.color.declined_bg_color, R.color.declined_text_color)

        val listStatus = listOf(
            statusApproved,
            statusPending,
            statusAll,
            statusLended,
            statusReturned,
            statusDeclined
        )

        val requests = listOf(
            ItemRequest(
                "SILYS",
                "191958-LBROB-001",
                "05/09/2025",
                "TOPICOS",
                "LBROB",
                "Solicitado para la presentación de un proyecto de investigacion ",
                statusApproved,
                getImageByLabel("LBROB")
            ),
            ItemRequest(
                "SISTEMA DE RIEGO AUTOMATIZADO",
                "191999-LBROB-004",
                "01/09/2025",
                "Electronica",
                "LBROB",
                "Solicitado para la presentación de un proyecto en la feria",
                statusPending,
                getImageByLabel("LBROB")
            ),
            ItemRequest(
                "CARRITO SEGUIDOR DE LINEA",
                "192020-LBROB-003",
                "20/09/2025",
                "Circuitos",
                "LBROB",
                "Solicitado para la presentación de un proyecto para la asignatura",
                statusReturned,
                getImageByLabel("LBROB")
            ),
            ItemRequest(
                "CASA INTELIGENTE",
                "191956-LBROB-001",
                "30/09/2025",
                "Electronica",
                "LBROB",
                "Solicitado para la presentación de un proyecto en la feria",
                statusDeclined,
                getImageByLabel("LBROB")
            ),
            ItemRequest(
                "SERVIDORES PARA EMPRENDEDORES",
                "191938-LBRYT-002",
                "25/08/2025",
                "Administración de servidores",
                "LBRYT",
                "Solicitado para la presentación de un proyecto en la feria",
                statusLended,
                getImageByLabel("LBRYT")
            )
        )

        rvStatus.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
        rvStatus.adapter = AdapterStatus(listStatus)

        rvRequest.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        rvRequest.adapter = AdapterRequest(requests, this)
    }

    fun getImageByLabel(label: String): Int {
        return when (label) {
            "LBROB" -> R.drawable.chip
            "LBRYT" -> R.drawable.servers
            else -> {
                R.drawable.ic_launcher_foreground
            }
        }
    }

    fun buttons() {
        val btnCreateRequest = findViewById<ImageButton>(R.id.btn_create_request)
        btnCreateRequest?.setOnClickListener {
            val intent = Intent(this, CreateRequest::class.java)
            intent.putExtra("TYPE_VIEW", "create_request")
            startActivity(intent)
        }
    }

    override fun onRequestClick(item: ItemRequest) {
        val intent = Intent(this, ShowRequest::class.java)
        intent.putExtra("item_request", item)
        startActivity(intent)
    }
}