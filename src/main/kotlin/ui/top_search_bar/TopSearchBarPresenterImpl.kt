package ui.top_search_bar

import kotlinx.coroutines.launch
import ui.base.BasePresenterImpl
import youtube.SearchForYouTubeVideos

class TopSearchBarPresenterImpl : TopSearchBarContract.Presenter, BasePresenterImpl<TopSearchBarContract.View>() {

  private val searchForYouTubeVideos = SearchForYouTubeVideos()

  override fun onStart() {
    //no-op
  }

  override fun onSearchQueryDecided(query: String) {
    launch {
      if(query.isNotBlank()) searchForYouTubeVideos(query)
    }
  }

  override fun onStop() {
    //no-op
  }
}
