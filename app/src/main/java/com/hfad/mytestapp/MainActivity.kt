package com.hfad.movie_search

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hfad.core.MenuItemClickListener
import com.hfad.media_details.MediaDetailsFragmentDirections
import com.hfad.poster.PosterFragmentDirections
import com.hfad.search.SearchFragmentDirections
import com.hfad.show_episodes.ShowEpisodesFragmentDirections
import com.hfad.show_episodes.ShowSeasonsFragmentDirections
import com.hfad.show_episodes.ShowEpisodeFragmentDirections
import com.hfad.liked.LikedFragmentDirections


import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), com.hfad.navigation.Navigator, MenuItemClickListener {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        Log.d("firebaseEnter","1")
        val auth = FirebaseAuth.getInstance()
        Log.d("firebaseEnter","2")
        if (auth.currentUser == null) {
            Log.d("firebaseEnter","3")

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.loginFragment)


//            val action = SearchFragmentDirections.navigateSearchToLogin()
//            findNavController(R.id.nav_host_fragment).navigate(action)



            // Navigate to LoginFragment
//            val navController = findNavController(R.id.nav_host_fragment)
//            Log.d("firebaseEnter","4")
//            navController.navigate(R.id.loginFragment)
            Log.d("firebaseEnter","5")
        }
    }

    override fun navigateSearchToMediaDetailsWithId(id: String) {
        val action = SearchFragmentDirections.navigateSearchToMediaDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
    override fun navigateSearchToLogin(){
        val action = SearchFragmentDirections.navigateSearchToLogin()
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

     override fun navigateShowSeasonsToLiked() {
        val action =
            ShowSeasonsFragmentDirections.navigateShowSeasonsToLiked()
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
    override fun navigateMediaDetailsToLiked(){
        val action =
            MediaDetailsFragmentDirections.navigateMediaDetailsToLiked()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateLikedToMediaDetailsWithId(id: String) {
        val action =
            LikedFragmentDirections.navigateLikedToMediaDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
    override fun navigateLikedToFromFbLiked(){
        val action =
            LikedFragmentDirections.navigateLikedToFromFbLiked()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
    override fun navigateLikedToSearchFriend(){
        val action = LikedFragmentDirections.navigateLikedToSearchFriend()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun onMenuItemClicked(itemId: Int) {
        when (itemId) {
            R.id.action_favorites -> {
                val currentFragment = supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.get(0)
                val fragmentName = currentFragment?.javaClass?.simpleName
                Log.d("MainActivity", "Favorites menu item clicked from fragment: $fragmentName")
                when (fragmentName) {
                    "MediaDetailsFragment" -> navigateMediaDetailsToLiked()
                    "ShowEpisodeFragment" -> navigateShowEpisodeToLiked()
                    "ShowEpisodesFragment" -> navigateShowEpisodesToLiked()
                    "ShowSeasonsFragment" -> navigateShowSeasonsToLiked()
                }
            }
        }
    }

}
