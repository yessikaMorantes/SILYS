package com.silys.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.silys.MainActivity
import com.silys.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class APIService {

    private val BASE_URL = "https://silys.pavazk.com/api/"

    suspend fun postJson(url: String, json: JSONObject, context: Context): JSONObject? =
        withContext(Dispatchers.IO) {

            val targetUrl = URL(BASE_URL + url)
            var connection: HttpURLConnection? = null
            val token = TokenManager(context).getAccessToken()

            try {
                connection = (targetUrl.openConnection() as HttpURLConnection).apply {

                    requestMethod = "POST"
                    connectTimeout = 15000
                    readTimeout = 15000
                    doInput = true
                    doOutput = true
                    useCaches = false

                    setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                    setRequestProperty("Accept", "application/json")

                    if (!token.isNullOrEmpty()) {
                        setRequestProperty("Authorization", "Bearer $token")
                    }
                }

                BufferedWriter(OutputStreamWriter(connection.outputStream, "UTF-8")).use { writer ->
                    writer.write(json.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == 401) {
                    connection.disconnect()
                    val newJson = JSONObject().apply {
                        put("refreshToken", TokenManager(context).getRefreshToken())
                    }
                    val newResponse = postJson("auth/refresh", newJson, context)
                    if (newResponse == null
                        || newResponse.optString("accessToken").isEmpty()
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
                        return@withContext postJson(url, json, context)
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
                    put("message", "Error en la conexión")
                }

                return@withContext JSONObject(responseText)

            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            } finally {
                connection?.disconnect()
            }
        }
}
