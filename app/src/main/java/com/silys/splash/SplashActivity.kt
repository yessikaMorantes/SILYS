package com.silys.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.silys.MainActivity
import com.silys.R
import com.silys.home.Home
import com.silys.utils.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        val splashScreen = installSplashScreen()

        lifecycleScope.launch {
            delay(3000L)
            val token = TokenManager(this@SplashActivity).getAccessToken()
            if (!token.isNullOrEmpty()) {
                startActivity(Intent(this@SplashActivity, Home::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            this@SplashActivity.finish()
        }
    }
}