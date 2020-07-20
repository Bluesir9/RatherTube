@file:Suppress("EXPERIMENTAL_API_USAGE")

package ui.central_content

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import song.Song

sealed class SearchEvent(open val searchQuery: String) {
  data class Loading(override val searchQuery: String): SearchEvent(searchQuery)
  /*
  The `songs` list can be empty.
   */
  data class Loaded(override val searchQuery: String, val songs: List<Song>): SearchEvent(searchQuery)
}

interface SearchEventTrigger {
  fun onSearchBegin(searchQuery: String)
  /*
  The `songsFound` list can be empty.
   */
  fun onSearchEnd(searchQuery: String, songsFound: List<Song>)
}

interface SearchEventListener {
  fun getSearchEvents(): Flow<SearchEvent>
}

object SearchResultsUICoordinator : SearchEventTrigger, SearchEventListener {
  private val eventChannel = BroadcastChannel<SearchEvent>(1)

  override fun onSearchBegin(searchQuery: String) {
    eventChannel.offer(SearchEvent.Loading(searchQuery))
  }

  override fun onSearchEnd(searchQuery: String, songsFound: List<Song>) {
    eventChannel.offer(SearchEvent.Loaded(searchQuery, songsFound))
  }

  override fun getSearchEvents(): Flow<SearchEvent> = eventChannel.asFlow()
}
