@file:Suppress("EXPERIMENTAL_API_USAGE")

package ui.central_content

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import utils.RepoResult
import youtube.YouTubeVideo

sealed class SearchEvent(open val searchQuery: String) {
  data class Loading(override val searchQuery: String): SearchEvent(searchQuery)
  /*
  The `videos` list can be empty.
   */
  data class Loaded(override val searchQuery: String, val videos: List<YouTubeVideo>): SearchEvent(searchQuery)
  data class LoadFailed(override val searchQuery: String, val failureMessage: String): SearchEvent(searchQuery)
}

interface SearchEventTrigger {
  fun onSearchBegin(searchQuery: String)
  /*
  The list can be empty.
   */
  fun onSearchEnd(searchQuery: String, videosResult: RepoResult<List<YouTubeVideo>>)
}

interface SearchEventsListener {
  fun getSearchEvents(): Flow<SearchEvent>
}

object SearchResultsUICoordinator : SearchEventTrigger, SearchEventsListener {
  private val eventChannel = BroadcastChannel<SearchEvent>(1)

  override fun onSearchBegin(searchQuery: String) {
    eventChannel.offer(SearchEvent.Loading(searchQuery))
  }

  override fun onSearchEnd(searchQuery: String, videosResult: RepoResult<List<YouTubeVideo>>) {
    when(videosResult) {
      is RepoResult.Success -> eventChannel.offer(SearchEvent.Loaded(searchQuery, videosResult.value))
      is RepoResult.Failure -> eventChannel.offer(SearchEvent.LoadFailed(searchQuery, videosResult.message))
    }
  }

  override fun getSearchEvents(): Flow<SearchEvent> = eventChannel.asFlow()
}
