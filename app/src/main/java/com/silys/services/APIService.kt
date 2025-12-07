package com.silys.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.animation.scaleOut
import com.silys.MainActivity
import com.silys.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class APIService {

    private val BASE_URL = "https://silys.pavazk.com/api/"
    //private val BASE_URL = "http://192.168.1.250:500/api/"

    suspend fun getJson(url: String, activity: Activity): JSONObject {
        return connectionManager(url, null, "GET", activity)
    }
    suspend fun logout(activity: Activity, message: String) {
        val json = JSONObject().apply {
            put("refreshToken", TokenManager(activity).getRefreshToken())
        }
        postJson("auth/logout", json, activity)
        TokenManager(activity).clearTokens()
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra("message", message)
        activity.startActivity(intent)
        activity.finish()
    }

    suspend fun postJson(url: String, json: JSONObject, activity: Activity): JSONObject {
        return connectionManager(url, json, "POST", activity)
    }

    private suspend fun connectionManager(
        url: String,
        json: JSONObject?,
        method: String,
        context: Activity
    ): JSONObject =
        withContext(Dispatchers.IO) {

            val targetUrl = URL(BASE_URL + url)
            var connection: HttpURLConnection? = null
            val token = TokenManager(context).getAccessToken()
            var output = true
            if (method == "GET") {
                output = false
            }

            try {
                connection = (targetUrl.openConnection() as HttpURLConnection).apply {

                    requestMethod = method
                    connectTimeout = 15000
                    readTimeout = 15000
                    doInput = true
                    doOutput = output
                    useCaches = false

                    if (method != "GET") {
                        setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                    }
                    setRequestProperty("Accept", "application/json")

                    if (!token.isNullOrEmpty()) {
                        setRequestProperty("Authorization", "Bearer $token")
                    }
                }
                if (method != "GET") {
                    Log.i("APIService send", json.toString())
                    BufferedWriter(
                        OutputStreamWriter(
                            connection.outputStream,
                            "UTF-8"
                        )
                    ).use { writer ->
                        writer.write(json.toString())
                        writer.flush()
                    }
                }

                val responseCode = connection.responseCode
                if (responseCode== 402) {
                    logout(context, "Token expirado")
                    return@withContext JSONObject()
                }
                if (responseCode == 401) {
                    connection.disconnect()
                    val newJson = JSONObject().apply {
                        put("refreshToken", TokenManager(context).getRefreshToken())
                    }
                    Log.i("APIService", "request new token!")
                    val newResponse = postJson("auth/refresh", newJson, context)
                    if (newResponse.optString("accessToken").isEmpty()
                        || newResponse.optString("refreshToken").isEmpty()
                    ) {
                        TokenManager(context).clearTokens()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        context.startActivity(intent)
                        return@withContext JSONObject().apply {
                            put("message", "Usuario no permitido")
                        }
                    } else {
                        TokenManager(context).saveTokens(
                            newResponse.optString("accessToken"),
                            newResponse.optString("refreshToken")
                        )
                        return@withContext connectionManager(url, json, method, context)
                    }
                }
                val stream = if (responseCode in 200..299) {
                    connection.inputStream
                } else {
                    connection.errorStream ?: return@withContext JSONObject().apply {
                        put("message", "Error en la conexión")
                    }
                }

                val responseText = stream.bufferedReader().use { it.readText() }

                if (responseText.isEmpty()) return@withContext JSONObject().apply {
                    put("message", "Error en la respuesta")
                }

                Log.i("APIService receive", responseText)
                return@withContext JSONObject(responseText)

            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext JSONObject().apply {
                    put("message", "Error en la conexión")
                }
            } finally {
                connection?.disconnect()
            }
        }
}
