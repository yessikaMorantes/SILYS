package com.silys.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.silys.MainActivity
import com.silys.home.adapter.AdapterRequest
import com.silys.home.adapter.AdapterStatus
import com.silys.home.adapter.OnRequestClickListener
import com.silys.home.item.ItemRequest
import com.silys.home.item.ItemStatus
import com.silys.create_request.CreateRequest
import com.silys.R
import com.silys.services.APIService
import com.silys.utils.UIUtils.Companion.showSnackBar
import com.silys.show_request.ShowRequest
import com.silys.utils.TokenManager
import kotlinx.coroutines.launch
import org.json.JSONObject

class Home : AppCompatActivity(), OnRequestClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val rootView = findViewById<View>(android.R.id.content)
        val rvStatus = findViewById<RecyclerView>(R.id.rv_status)
        val rvRequest = findViewById<RecyclerView>(R.id.rv_requests)
        val title = findViewById<TextView>(R.id.title)
        val subtitle = findViewById<TextView>(R.id.sub_title)
        val userName = findViewById<TextView>(R.id.user_name)
        val userCode = findViewById<TextView>(R.id.user_code)
        val prefixUserName = findViewById<TextView>(R.id.prefix_user_name)

        buttons()


        rvStatus.layoutManager = LinearLayoutManager(
            this@Home,
            LinearLayoutManager.HORIZONTAL, false
        )
        lifecycleScope.launch{
            val response = APIService().getJson("android/home", this@Home)
            try{
                title.text = response.optString("title")
                subtitle.text = response.optString("sub_title")
                userName.text = response.optString("user_name")
                userCode.text = response.optString("user_code")
                prefixUserName.text = response.optString("prefix_user_name")

                val itemStatusArray = response.getJSONArray("item_status")
                val itemStatusList = mutableListOf<JSONObject>()

                for (i in 0 until itemStatusArray.length()) {
                    val obj = itemStatusArray.getJSONObject(i)
                    itemStatusList.add(obj)
                }
                rvStatus.adapter = AdapterStatus(itemStatusList)
            }catch (e: Exception){
                var message: String = response.optString("message");
                if(message.isBlank()){
                    message = response.optString("error");
                }
                e.printStackTrace()
                Toast.makeText(this@Home, message, Toast.LENGTH_SHORT).show()
            }

        }
        //"#ADEAE0".toColorInt()

        val statusAll =
            ItemStatus("Todo", R.color.all_bg_color, R.color.all_text_color)
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

        //val listStatus = listOf(
        //    statusApproved,
        //    statusPending,
        //    statusAll,
        //    statusLended,
        //    statusReturned,
        //    statusDeclined
        //)

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



        rvRequest.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        rvRequest.adapter = AdapterRequest(requests, this)
        rootView.showSnackBar("Has iniciado sesión")
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

        val btnLogout = findViewById<ImageButton>(R.id.btn_toolbar)
        btnLogout?.setOnClickListener {
            lifecycleScope.launch {
                val json = JSONObject().apply {
                    put("refreshToken", TokenManager(this@Home).getRefreshToken())
                }
                val api = APIService()
                val response = api.postJson("auth/logout", json, this@Home)
                TokenManager(this@Home).clearTokens()
                startActivity(Intent(this@Home, MainActivity::class.java))
                this@Home.finish()
            }
        }
    }

    override fun onRequestClick(item: ItemRequest) {
        val intent = Intent(this, ShowRequest::class.java)
        intent.putExtra("item_request", item)
        startActivity(intent)
    }


    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        if(false){
            super.onBackPressed()
        }
    }
}