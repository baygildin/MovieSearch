package com.hfad.mytestapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.hfad.search.SearchFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), com.hfad.navigation.Navigator {

    private val SPLASH_DELAY: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
    }

    override fun navigateToMoveDetailsWithId(id: String) {
        val action = SearchFragmentDirections.navigateToMoveDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}