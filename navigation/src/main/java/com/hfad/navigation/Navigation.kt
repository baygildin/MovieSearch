package com.hfad.navigation

interface Navigator {
    fun navigateSearchToMovieDetailsWithId(id: String)
    fun navigateShowEpisodesToSearchWithId(id: String)
    fun navigateShowEpisodesToMovieDetailsWithId(id: String)
    fun navigatePosterToMovieDetailsWithId(id: String)
    fun navigateMovieDetailsToSearchWithId(id: String)
    fun navigateMovieDetailsToPosterWithId(id: String)
    fun navigateMovieDetailsToShowEpisodesWithId(id: String)
}
