package com.hfad.mytestapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.hfad.media_details.MediaDetailsFragmentDirections
import com.hfad.poster.PosterFragmentDirections
import com.hfad.search.SearchFragmentDirections
import com.hfad.show_episodes.ShowEpisodesFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), com.hfad.navigation.Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

    }
    override fun navigateSearchToMediaDetailsWithId(id: String) {
        val action = SearchFragmentDirections.navigateSearchToMediaDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateShowEpisodesToSearchWithId(id: String){
        val action = ShowEpisodesFragmentDirections.navigateShowEpisodesToMediaDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateShowEpisodesToMediaDetailsWithId(id: String){
        val action = ShowEpisodesFragmentDirections.navigateShowEpisodesToMediaDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigatePosterToMediaDetailsWithId(id: String){
        val action = PosterFragmentDirections.navigatePosterToMediaDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateMediaDetailsToSearchWithId(id: String){
        val action = MediaDetailsFragmentDirections.navigateMediaDetailsToSearchWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateMediaDetailsToPosterWithId(id: String){
        val action = MediaDetailsFragmentDirections.navigateMediaDetailsToPosterWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateMediaDetailsToShowEpisodesWithId(id: String){
        val action = MediaDetailsFragmentDirections.navigateMediaDetailsToShowEpisodesWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}