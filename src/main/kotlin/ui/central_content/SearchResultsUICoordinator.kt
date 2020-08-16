@file:Suppress("EXPERIMENTAL_API_USAGE")

package ui.central_content

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import youtube.YouTubeVideo

sealed class SearchEvent(open val searchQuery: String) {
  data class Loading(override val searchQuery: String): SearchEvent(searchQuery)
  /*
  The `videos` list can be empty.
   */
  data class Loaded(override val searchQuery: String, val videos: List<YouTubeVideo>): SearchEvent(searchQuery)
}

interface SearchEventTrigger {
  fun onSearchBegin(searchQuery: String)
  /*
  The `videosFound` list can be empty.
   */
  fun onSearchEnd(searchQuery: String, videosFound: List<YouTubeVideo>)
}

interface SearchEventsListener {
  fun getSearchEvents(): Flow<SearchEvent>
}

object SearchResultsUICoordinator : SearchEventTrigger, SearchEventsListener {
  private val eventChannel = BroadcastChannel<SearchEvent>(1)

  override fun onSearchBegin(searchQuery: String) {
    eventChannel.offer(SearchEvent.Loading(searchQuery))
  }

  override fun onSearchEnd(searchQuery: String, videosFound: List<YouTubeVideo>) {
    eventChannel.offer(SearchEvent.Loaded(searchQuery, videosFound))
  }

  override fun getSearchEvents(): Flow<SearchEvent> = eventChannel.asFlow()
}
