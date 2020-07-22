package ui.top_search_bar

import kotlinx.coroutines.launch
import song.SearchForSongs
import ui.base.BasePresenterImpl

class TopSearchBarPresenterImpl : TopSearchBarContract.Presenter, BasePresenterImpl() {

  private val searchForSongs = SearchForSongs()

  override fun onStart() {
    //no-op
  }

  override fun onSearchQueryDecided(query: String) {
    launch {
      if(query.isNotBlank()) searchForSongs(query)
    }
  }

  override fun onStop() {
    //no-op
  }
}
