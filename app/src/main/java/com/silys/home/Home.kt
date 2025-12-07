package com.silys.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.silys.MainActivity
import com.silys.R
import com.silys.create_request.CreateRequest
import com.silys.home.adapter.AdapterRequest
import com.silys.home.adapter.AdapterStatus
import com.silys.home.adapter.OnRequestClickListener
import com.silys.services.APIService
import com.silys.utils.TokenManager
import com.silys.utils.UIUtils.Companion.hideLoading
import com.silys.utils.UIUtils.Companion.showLoading
import com.silys.utils.UIUtils.Companion.showSnackBar
import com.silys.utils.Utils
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
        rootView.showLoading()

        buttons()

        rvStatus.layoutManager = LinearLayoutManager(
            this@Home,
            LinearLayoutManager.HORIZONTAL, false
        )
        rvRequest.layoutManager = LinearLayoutManager(
            this@Home,
            LinearLayoutManager.VERTICAL, false
        )
        lifecycleScope.launch{
            val response = APIService().getJson("android/home", this@Home)
            try{
                title.text = response.optString("title")
                subtitle.text = response.optString("sub_title")
                userName.text = response.optString("user_name")
                userCode.text = response.optString("user_code")
                prefixUserName.text = response.optString("prefix_user_name")


                rvStatus.adapter = AdapterStatus(Utils().convertJSONArrayToListJSON(response.getJSONArray("item_status")))

                val itemRequestList = Utils().convertJSONArrayToListJSON(response.getJSONArray("requests"))
                if(!itemRequestList.isEmpty()){
                    rvRequest.adapter = AdapterRequest(itemRequestList, this@Home, this@Home)
                } else {
                    rvRequest.visibility = View.GONE
                    findViewById<LottieAnimationView>(R.id.lottie_empty).visibility = View.VISIBLE
                    val msg = findViewById<TextView>(R.id.text_empty)
                    msg.visibility = View.VISIBLE
                    msg.text = response.optString("msg")
                }
            }catch (e: Exception){
                var message: String = response.optString("message");
                if(message.isBlank()){
                    message = response.optString("error");
                }
                e.printStackTrace()
                Toast.makeText(this@Home, message, Toast.LENGTH_SHORT).show()
            }
            rootView.hideLoading()
            rootView.showSnackBar(intent.getStringExtra("message").orEmpty())
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
                APIService().logout(this@Home, "")
            }
        }
    }

    override fun onRequestClick(item: JSONObject) {
        //val intent = Intent(this, ShowRequest::class.java)
        //intent.putExtra("item_request", item)
        //startActivity(intent)
    }


    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        if(false){
            super.onBackPressed()
        }
    }
}