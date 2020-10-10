package ui.central_content

import config.StringResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import logging.Logger
import logging.LoggerImpl
import playback.queue.AddToPlaybackQueue
import playback.usecases.Play
import ui.base.BasePresenterImpl
import ui.central_content.SearchEvent.Loaded
import ui.central_content.SearchEvent.Loading
import youtube.YouTubeVideo

@ExperimentalCoroutinesApi
class CentralContentPresenterImpl : CentralContentContract.Presenter, BasePresenterImpl<CentralContentContract.View>() {

  private val logger: Logger = LoggerImpl(CentralContentPresenterImpl::class.simpleName!!)
  private val searchEventsListener: SearchEventsListener = SearchResultsUICoordinator
  private val generateVM: CentralContentVMGenerator = CentralContentVMGeneratorImpl()
  private val play: Play = Play()
  private val addToPlaybackQueue = AddToPlaybackQueue()

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

  override fun onPlaySearchResult(id: String) {
    val video = findVideoFromLoadedVideos(id)
    if (video != null) {
      play(video)
    } else {
      logger.error("Failed to find video from last loaded videos. Can't play search result with id = $id")
      showFloatingMessage(StringResource.FAILED_TO_PLAY_TRACK)
    }
  }

  override fun onAddSearchResultToQueue(id: String) {
    val video = findVideoFromLoadedVideos(id)
    if(video != null) {
      addToPlaybackQueue(video)
      showFloatingMessage(StringResource.TRACK_ADDED_TO_PLAYBACK_QUEUE)
    } else {
      logger.error("Failed to find video from last loaded videos. Can't add to queue, search result with id = $id")
      showFloatingMessage(StringResource.FAILED_TO_ADD_TRACK_TO_PLAYBACK_QUEUE)
    }
  }

  private fun findVideoFromLoadedVideos(id: String): YouTubeVideo? {
    when(val searchEvent = lastSearchEvent) {
      is Loading -> {
        logger.error("Unexpected encounter of search result click event. lastVM = $lastVM")
        return null
      }
      is Loaded -> {
        val clickedVideo = searchEvent.videos.firstOrNull { video -> video.id == id }
        if(clickedVideo == null) {
          logger.error("Unable to find clicked song. lastVM = $lastVM, searchEvent = $searchEvent")
        }
        return clickedVideo
      }
      else -> {
        logger.error("Unexpected search event = $lastSearchEvent. Can't find video with id = $id")
        return null
      }
    }
  }

  override fun onStop() {
    //no-op
  }
}
