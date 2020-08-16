package ui.central_content

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import logging.Logger
import logging.LoggerImpl
import ui.base.BasePresenterImpl
import ui.central_content.SearchEvent.Loaded
import ui.central_content.SearchEvent.Loading
import youtube.YouTubeVideo
import youtube.YouTubeVideoPlaybackCoordinator
import youtube.YouTubeVideoPlayer

@ExperimentalCoroutinesApi
class CentralContentPresenterImpl : CentralContentContract.Presenter, BasePresenterImpl<CentralContentContract.View>() {

  private val logger: Logger = LoggerImpl(CentralContentPresenterImpl::class.simpleName!!)
  private val searchEventsListener: SearchEventsListener = SearchResultsUICoordinator
  private val generateVM: CentralContentVMGenerator = CentralContentVMGeneratorImpl()
  private val videoPlayer: YouTubeVideoPlayer = YouTubeVideoPlaybackCoordinator

  private lateinit var lastSearchEvent: SearchEvent
  private lateinit var lastVM: CentralContentVM

  override fun onStart() {
    searchEventsListener
      .getSearchEvents()
      .onEach { lastSearchEvent = it }
      .map { generateVM(it) }
      .onEach { lastVM = it }
      .onEach { view.render(it) }
      .launchIn(this)
  }

  override fun onSearchResultClick(id: String) {
    when(val searchEvent = lastSearchEvent) {
      is Loading -> logger.error("Unexpected encounter of search result click event. lastVM = $lastVM")
      is Loaded -> {
        val clickedVideo = searchEvent.videos.firstOrNull { video -> video.id == id }
        if (clickedVideo != null) playVideo(clickedVideo)
        else logger.error("Unable to find clicked song. lastVM = $lastVM, searchEvent = $searchEvent")
      }
    }
  }

  private fun playVideo(video: YouTubeVideo) {
    launch {
      videoPlayer.playRequested(video)
    }
  }

  override fun onStop() {
    //no-op
  }
}
