package com.hfad.mytestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hfad.mytestapp.R
import android.content.Intent
import android.os.Handler
import android.os.Looper
class MainActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Handler(Looper.getMainLooper()).postDelayed({
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }, SPLASH_DELAY)
    }
}




