package com.hfad.mytestapp.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.hfad.mytestapp.MainActivity
import com.hfad.mytestapp.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        // Задержка отображения Splash Screen и перехода к следующей активности
        Handler().postDelayed({
            // Создаем намерение для перехода к следующей активности (например, MainActivity)
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // Закрываем Splash Activity, чтобы пользователь не мог вернуться назад к нему
        }, SPLASH_DELAY)
    }

    companion object {
        // Время задержки отображения Splash Screen
        private const val SPLASH_DELAY: Long = 3000 // 3 секунды
    }
}

