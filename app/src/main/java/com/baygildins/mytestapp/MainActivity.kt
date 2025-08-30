package com.baygildins.movie_search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import com.baygildins.core.MenuItemClickListener
import com.baygildins.friend_requests.FriendRequestsFragmentDirections
import com.baygildins.friends_list.FriendsListFragmentDirections
import com.baygildins.liked.LikedFragmentDirections
import com.baygildins.media_details.MediaDetailsFragmentDirections
import com.baygildins.poster.PosterFragmentDirections
import com.baygildins.search.SearchFragmentDirections
import com.baygildins.search_friend.SearchFriendFragmentDirections
import com.baygildins.show_episodes.ShowEpisodeFragmentDirections
import com.baygildins.show_episodes.ShowEpisodesFragmentDirections
import com.baygildins.show_episodes.ShowSeasonsFragmentDirections
import com.baygildins.show_media_of_friend.ShowMediaOfFriendFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), com.baygildins.navigation.Navigator,
    MenuItemClickListener {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
    }

    override fun navigateSearchToMediaDetailsWithId(id: String) {
        val action = SearchFragmentDirections.navigateSearchToMediaDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateSearchToLogin() {
        val action = SearchFragmentDirections.navigateSearchToLogin()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigatePosterToMediaDetailsWithId(id: String) {
        val action = PosterFragmentDirections.navigatePosterToMediaDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateMediaDetailsToSearchWithId(id: String) {
        val action = MediaDetailsFragmentDirections.navigateMediaDetailsToSearchWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateMediaDetailsToPosterWithId(id: String) {
        val action = MediaDetailsFragmentDirections.navigateMediaDetailsToPosterWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateMediaDetailsToShowSeasonsWithId(id: String) {
        val action = MediaDetailsFragmentDirections.navigateMediaDetailsToShowSeasonsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateShowEpisodesToShowEpisode(
        title: String,
        seasonNumber: String,
        episodeNumber: String
    ) {
        val action =
            ShowEpisodesFragmentDirections.navigateShowEpisodesToShowEpisode(
                title,
                seasonNumber,
                episodeNumber
            )
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

    override fun navigateMediaDetailsToLiked() {
        val action =
            MediaDetailsFragmentDirections.navigateMediaDetailsToLiked()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateLikedToMediaDetailsWithId(id: String) {
        val action =
            LikedFragmentDirections.navigateLikedToMediaDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateLikedToSearchFriend() {
        val action = LikedFragmentDirections.navigateLikedToSearchFriend()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateLikedToFriendsList() {
        val action = LikedFragmentDirections.actionLikedToFriendsListFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateShowMediaOfFriendToMediaDetailsWithId(id: String) {
        val action =
            ShowMediaOfFriendFragmentDirections.navigateShowMediaOfFriendToMediaDetailsWithId(id)
        findNavController(R.id.nav_host_fragment).navigate(action)

    }

    override fun actionsearchFriendtoFriendsListFragment() {
        val action = SearchFriendFragmentDirections.actionsearchFriendtoFriendsListFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }


    override fun navigateFriendsListFragmentToShowMediaOfFriendId(userId: String) {
        val action = FriendsListFragmentDirections.navigateFriendsListFragmentToShowMediaOfFriendId(
            userId
        )
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun action_Login_to_FriendsListFragment() {
        val action = LoginFragmentDirections.actionLogintoFriendsListFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun action_Liked_to_FriendsListFragment() {
        val action = LikedFragmentDirections.actionLikedToFriendsListFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun action_Show_Episode_to_FriendsListFragment() {
        val action = ShowEpisodeFragmentDirections.actionShowEpisodetoFriendsListFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun action_Show_episodes_to_FriendsListFragment() {
        val action = ShowEpisodesFragmentDirections.actionShowepisodestoFriendsListFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun action_Show_seasons_to_FriendsListFragment() {
        val action = ShowSeasonsFragmentDirections.actionShowSeasonsToFriendsListFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun action_media_details_to_FriendsListFragment() {
        val action = MediaDetailsFragmentDirections.actionMediaDetailsToFriendsListFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun action_showMediaOfFriend_to_likedFragment() {
        val action = ShowMediaOfFriendFragmentDirections.actionShowMediaOfFriendToLikedFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun action_searchFriend_to_likedFragment() {
        val action = SearchFriendFragmentDirections.actionSearchFriendToLikedFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun action_login_to_likedFragment() {
        val action = LoginFragmentDirections.actionLoginToLikedFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun actionFriendListToLikedFragment() {
        val action = FriendsListFragmentDirections.actionFriendListToLikedFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateFriendsListFragmentToFriendRequestsFragment() {
        val action =
            FriendsListFragmentDirections.navigateFriendsListFragmentToFriendRequestsFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateFriendListToSearchFriend() {
        val action = FriendsListFragmentDirections.navigateFriendListToSearchFriend()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun actionFriendRequestsToFriendsListFragment() {
        val action = FriendRequestsFragmentDirections.actionFriendRequestsToFriendsListFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun actionFriendListFragmentToLikedFragment() {
        val action = FriendsListFragmentDirections.actionFriendListFragmentToLikedFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun actionFriendRequestsToLikedFragment() {
        val action = FriendRequestsFragmentDirections.actionFriendRequestsToLikedFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun actionSearchFriendFragmentToLoginFragment() {
        val action = SearchFriendFragmentDirections.actionSearchFriendFragmentToLoginFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateLikedToLogin() {
        val action = LikedFragmentDirections.navigateLikedToLogin()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun actionShowEpisodeFragmentToLoginFragment() {
        val action = ShowEpisodeFragmentDirections.actionShowEpisodeFragmentToLoginFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun actionShowEpisodesFragmentToLoginFragment() {
        val action = ShowEpisodesFragmentDirections.actionShowEpisodesFragmentToLoginFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun actionShowSeasonsFragmentToLoginFragment() {
        val action = ShowSeasonsFragmentDirections.actionShowSeasonsFragmentToLoginFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun actionMediaDetailsFragmentToLoginFragment() {
        val action = MediaDetailsFragmentDirections.actionMediaDetailsFragmentToLoginFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun actionFriendRequestsFragmentToLoginFragment() {
        val action = FriendRequestsFragmentDirections.actionFriendRequestsFragmentToLoginFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateFriendRequestToShowMediaOfFriendId(userId: String) {
        val action =
            FriendRequestsFragmentDirections.actionFriendRequestToShowMediaOfFriendId(userId)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateFriendsListFragmentToSearchFragment() {
        val action = FriendsListFragmentDirections.navigateFriendsListFragmentToSearchFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }


    override fun onMenuItemClicked(itemId: Int) {
        when (itemId) {
            R.id.action_friends -> {
                val currentFragment =
                    supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.get(
                        0
                    )
                val fragmentName = currentFragment?.javaClass?.simpleName

                if (auth.currentUser == null) {
                    when (fragmentName) {
                        "SearchFriendFragment" -> actionSearchFriendFragmentToLoginFragment()
                        "LikedFragment" -> navigateLikedToLogin()
                        "ShowEpisodeFragment" -> actionShowEpisodeFragmentToLoginFragment()
                        "ShowEpisodesFragment" -> actionShowEpisodesFragmentToLoginFragment()
                        "ShowSeasonsFragment" -> actionShowSeasonsFragmentToLoginFragment()
                        "MediaDetailsFragment" -> actionMediaDetailsFragmentToLoginFragment()
                        "FriendRequestsFragment" -> actionFriendRequestsFragmentToLoginFragment()
                    }
                } else {
                    when (fragmentName) {
                        "SearchFriendFragment" -> actionsearchFriendtoFriendsListFragment()
                        "LoginFragment" -> action_Login_to_FriendsListFragment()
                        "LikedFragment" -> action_Liked_to_FriendsListFragment()
                        "ShowEpisodeFragment" -> action_Show_Episode_to_FriendsListFragment()
                        "ShowEpisodesFragment" -> action_Show_episodes_to_FriendsListFragment()
                        "ShowSeasonsFragment" -> action_Show_seasons_to_FriendsListFragment()
                        "MediaDetailsFragment" -> action_media_details_to_FriendsListFragment()
                        //"FriendRequestsFragment" -> actionFriendRequestsToFriendsListFragment()
                        "FriendRequestsFragment" -> actionFriendRequestsToLikedFragment()

                    }
                }
            }

            R.id.action_favorites -> {
                val currentFragment =
                    supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.get(
                        0
                    )
                val fragmentName = currentFragment?.javaClass?.simpleName
                when (fragmentName) {
                    "MediaDetailsFragment" -> navigateMediaDetailsToLiked()
                    "ShowEpisodeFragment" -> navigateShowEpisodeToLiked()
                    "ShowEpisodesFragment" -> navigateShowEpisodesToLiked()
                    "ShowSeasonsFragment" -> navigateShowSeasonsToLiked()
                    "ShowMediaOfFriendFragment" -> action_showMediaOfFriend_to_likedFragment()
                    "SearchFriendFragment" -> action_searchFriend_to_likedFragment()
                    "LoginFragment" -> action_login_to_likedFragment()
                    "FriendListFragment" -> actionFriendListFragmentToLikedFragment()
                }
            }


        }
    }
}