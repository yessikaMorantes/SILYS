package com.silys.show_request

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.silys.R
import com.silys.home.Home
import com.silys.home.adapter.AdapterCheck
import com.silys.services.APIService
import com.silys.utils.UIUtils.Companion.hideLoading
import com.silys.utils.UIUtils.Companion.showLoading
import com.silys.utils.UIUtils.Companion.showSnackBar
import com.silys.utils.Utils
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class ShowRequest : AppCompatActivity() {
    private lateinit var adapter : AdapterCheck;
    private lateinit var rootView: View;
    private lateinit var response: JSONObject;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_request)
        setToolbar()
        response = JSONObject(intent.getStringExtra("json").orEmpty())
        findViewById<TextView>(R.id.title).text = response.optString("title").orEmpty()
        findViewById<TextView>(R.id.sub_title).text = response.optString("sub_title").orEmpty()
        findViewById<TextView>(R.id.user_name).text = response.optString("user_name").orEmpty()
        findViewById<TextView>(R.id.user_code).text = response.optString("user_code").orEmpty()
        findViewById<TextView>(R.id.prefix_user_name).text =
            response.optString("prefix_user_name").orEmpty()
        findViewById<TextView>(R.id.tv_project_name).text =
            response.optString("name_project").orEmpty()
        findViewById<TextView>(R.id.tv_settled_request).text = ""
        findViewById<TextView>(R.id.tv_request_id).text =
            response.optString("request_label").orEmpty()
        findViewById<TextView>(R.id.tv_label_items).text =
            response.optString("label_items").orEmpty()
        findViewById<TextView>(R.id.tv_extra_info).text =
            Html.fromHtml(response.optString("extra_info").orEmpty())
        val rvImplements = findViewById<RecyclerView>(R.id.rv_check)
        rvImplements.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        adapter = AdapterCheck(
            Utils().convertJSONArrayToList(response.getJSONArray("list_implements")),
            response.getBoolean("isCheckBox")
        )
        rvImplements.adapter = adapter
            findViewById<LinearLayout>(R.id.ll_filed).visibility =
            if (response.getBoolean("showFiled")) {
                View.VISIBLE
            } else {
                View.GONE
            }
        findViewById<LinearLayout>(R.id.ll_commentary).visibility =
            if (response.getBoolean("showCommentary")) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }

        rootView = findViewById<View>(android.R.id.content)
        buttons(response)
    }

    fun buttons(response: JSONObject) {
        findViewById<LinearLayout>(R.id.ll_buttons).visibility =
            if (response.getBoolean("showButtons")) {
                View.VISIBLE
            } else {
                View.GONE
            }
        if (!response.getBoolean("showButtons")) {
            return
        }
        if (response.has("textButtonDecline")) {
            findViewById<Button>(R.id.btn_red).text = response.getString("textButtonDecline")
        }
        if (response.has("textButtonApprove")) {
            findViewById<Button>(R.id.btn_green).text = response.getString("textButtonApprove")
        }
        findViewById<Button>(R.id.btn_red).setOnClickListener {
            lifecycleScope.launch {
                rootView.showLoading()
                val response = APIService().postJson("android/decline-request/${response.getString("code")}", JSONObject(), this@ShowRequest)
                rootView.hideLoading()
                val intent = Intent(this@ShowRequest, Home::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("message", response.optString("message"))
                startActivity(intent)
                this@ShowRequest.finish()
            }
        }
        findViewById<Button>(R.id.btn_green).setOnClickListener {
            lifecycleScope.launch {
                if(adapter.getSelectedItems().isEmpty()){
                    rootView.showSnackBar("Por favor aprobar al menos un implemento")
                    return@launch;
                }
                rootView.showLoading()
                val json = JSONObject().apply {
                    put("approvedImplements", JSONArray(adapter.getSelectedItems()))
                }
                val response = APIService().postJson("android/approve-request/${response.getString("code")}", json, this@ShowRequest)
                rootView.hideLoading()
                val intent = Intent(this@ShowRequest, Home::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("message", response.optString("message"))
                startActivity(intent)
                this@ShowRequest.finish()
            }
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