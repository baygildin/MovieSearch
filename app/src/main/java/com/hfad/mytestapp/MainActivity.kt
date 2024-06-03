package com.hfad.mytestapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.hfad.media_details.MediaDetailsFragmentDirections
import com.hfad.poster.PosterFragmentDirections
import com.hfad.search.SearchFragmentDirections
import com.hfad.show_episodes.ShowEpisodesFragmentDirections
import com.hfad.show_episodes.ShowSeasonsFragmentDirections
import com.hfad.show_episodes.ShowEpisodeFragmentDirections
//import com.hfad.liked.LikedFragmentDirections


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

    override fun navigateMediaDetailsToShowSeasonsWithId(id: String){
        val action = MediaDetailsFragmentDirections.navigateMediaDetailsToShowSeasonsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
    override fun navigateShowEpisodesToShowEpisode(title: String, seasonNumber: String, episodeNumber: String) {
        val action =
            ShowEpisodesFragmentDirections.navigateShowEpisodesToShowEpisode(title, seasonNumber, episodeNumber)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
    override fun navigateShowSeasonsToShowEpisodes(title: String, seasonNumber: String) {
        val action =
            ShowSeasonsFragmentDirections.navigateShowSeasonsToShowEpisodes(title, seasonNumber)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }


//////////

    override fun navigateShowSeasonsToLiked() {
        val action =
            ShowSeasonsFragmentDirections.navigateShowSeasonsToLiked()
        findNavController(R.id.nav_host_fragment).navigate(action)

    }
    override fun navigateMediaDetailsToLiked(){
        val action = MediaDetailsFragmentDirections.navigateMediaDetailsToLiked()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateShowEpisodeToLiked() {
        val action =
            ShowEpisodeFragmentDirections.navigateShowEpisodeToLiked()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateShowEpisodesToLiked() {
        val action =
            ShowEpisodesFragmentDirections.navigateShowEpisodesToLiked()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}
