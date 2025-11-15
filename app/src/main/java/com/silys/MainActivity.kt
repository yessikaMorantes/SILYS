package com.silys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.silys.home.Home
import com.silys.services.APIService
import com.silys.utils.TokenManager
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : ComponentActivity() {

    private lateinit var etCode: TextInputEditText
    private lateinit var etDni: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnSignIn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        etCode = findViewById(R.id.etCode)
        etDni = findViewById(R.id.etDni)
        etPassword = findViewById(R.id.etPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
    }

    private fun setupListeners() {
        btnSignIn.setOnClickListener {
            val code = etCode.text.toString().trim()
            val dni = etDni.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (code.isEmpty() || dni.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val json = JSONObject().apply {
                put("code", code)
                put("dni", dni)
                put("password", pass)
            }

            login(json)
        }
    }

    private fun login(json: JSONObject) {
        lifecycleScope.launch {
            val api = APIService()
            val response = api.postJson("auth/login", json, this@MainActivity)

            if (response.optString("accessToken").isNullOrEmpty()
                || response.optString("refreshToken").isNullOrEmpty()
            ) {
                Toast.makeText(this@MainActivity, response?.optString("message"), Toast.LENGTH_SHORT)
                    .show()
            } else {
                val tokenManager = TokenManager(this@MainActivity)
                tokenManager.saveTokens(
                    response.optString("accessToken"),
                    response.optString("refreshToken")
                )
                val intent = Intent(this@MainActivity, Home::class.java)
                startActivity(intent)
                this@MainActivity.finish()
            }
        }
    }
}
