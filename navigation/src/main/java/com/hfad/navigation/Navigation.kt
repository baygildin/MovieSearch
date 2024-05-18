package com.hfad.navigation

interface Navigator {
    fun navigateSearchToMediaDetailsWithId(id: String)
    fun navigateShowEpisodesToSearchWithId(id: String)
    fun navigateShowEpisodesToMediaDetailsWithId(id: String)
    fun navigatePosterToMediaDetailsWithId(id: String)
    fun navigateMediaDetailsToSearchWithId(id: String)
    fun navigateMediaDetailsToPosterWithId(id: String)
    fun navigateMediaDetailsToShowEpisodesWithId(id: String)
}
