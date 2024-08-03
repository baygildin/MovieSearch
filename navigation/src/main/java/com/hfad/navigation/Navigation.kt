package com.hfad.navigation

interface Navigator {
    fun navigateSearchToMediaDetailsWithId(id: String)
    fun navigatePosterToMediaDetailsWithId(id: String)
    fun navigateMediaDetailsToSearchWithId(id: String)
    fun navigateMediaDetailsToPosterWithId(id: String)
    fun navigateMediaDetailsToShowSeasonsWithId(id: String)
    fun navigateShowEpisodesToShowEpisode(
        title: String,
        seasonNumber: String,
        episodeNumber: String
    )

    fun navigateShowSeasonsToShowEpisodes(title: String, seasonNumber: String)
    fun navigateShowSeasonsToLiked()
    fun navigateMediaDetailsToLiked()
    fun navigateShowEpisodeToLiked()
    fun navigateShowEpisodesToLiked()
    fun navigateLikedToMediaDetailsWithId(id: String)
    fun navigateSearchToLogin()
    fun navigateLikedToFromFbLiked()
    fun navigateLikedToSearchFriend()
    fun navigateLikedToFriendsList()
    fun navigateFriendsListFragmentToShowMediaOfFriendId(friendEmail: String)
    fun navigateShowMediaOfFriendToMediaDetailsWithId(id: String)
    fun actionsearchFriendtoFriendsListFragment()
    fun action_from_fb_to_FriendsListFragment()
    fun action_Login_to_FriendsListFragment()
    fun action_Liked_to_FriendsListFragment()
    fun action_Show_Episode_to_FriendsListFragment()
    fun action_Show_episodes_to_FriendsListFragment()
    fun action_Show_seasons_to_FriendsListFragment()
    fun action_media_details_to_FriendsListFragment()
    fun action_showMediaOfFriend_to_likedFragment()
    fun action_searchFriend_to_likedFragment()
    fun action_fromFb_to_likedFragment()
    fun action_login_to_likedFragment()
    fun actionFriendListToLikedFragment()
    fun navigateFriendsListFragmentToFriendRequestsFragment()
    fun navigateFriendListToSearchFriend()
    fun actionFriendRequestsToFriendsListFragment()
    fun actionFriendListFragmentToLikedFragment()
    fun actionFriendRequestsToLikedFragment()

    fun navigateLikedToLogin()
    fun actionSearchFriendFragmentToLoginFragment()
    fun actionShowEpisodeFragmentToLoginFragment()
    fun actionShowEpisodesFragmentToLoginFragment()
    fun actionShowSeasonsFragmentToLoginFragment()
    fun actionMediaDetailsFragmentToLoginFragment()
    fun actionFriendRequestsFragmentToLoginFragment()


}
