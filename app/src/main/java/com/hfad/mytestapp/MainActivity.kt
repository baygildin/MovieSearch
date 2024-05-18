package com.hfad.mytestapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.hfad.movie_details.MovieDetailsFragmentDirections
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
    override fun navigateSearchToMovieDetailsWithId(id: String) {
        val action = SearchFragmentDirections.navigateSearchToMovieDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateShowEpisodesToSearchWithId(id: String){
        val action = ShowEpisodesFragmentDirections.navigateShowEpisodesToMovieDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateShowEpisodesToMovieDetailsWithId(id: String){
        val action = ShowEpisodesFragmentDirections.navigateShowEpisodesToMovieDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigatePosterToMovieDetailsWithId(id: String){
        val action = PosterFragmentDirections.navigatePosterToMovieDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateMovieDetailsToSearchWithId(id: String){
        val action = MovieDetailsFragmentDirections.navigateMovieDetailsToSearchWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateMovieDetailsToPosterWithId(id: String){
        val action = MovieDetailsFragmentDirections.navigateMovieDetailsToPosterWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateMovieDetailsToShowEpisodesWithId(id: String){
        val action = MovieDetailsFragmentDirections.navigateMovieDetailsToShowEpisodesWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}