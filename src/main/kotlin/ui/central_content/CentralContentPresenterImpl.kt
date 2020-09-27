package ui.central_content

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import logging.Logger
import logging.LoggerImpl
import playback.queue.PlaybackQueue
import playback.queue.PlaybackQueueImpl
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
  private val playbackQueue: PlaybackQueue = PlaybackQueueImpl

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
    logger.debug("onPlaySearchResult")
    val video = findVideoFromLoadedVideos(id)
    if (video != null) {
      play(video)
    } else {
      logger.error("Failed to find video from last loaded videos. Can't play search result with id = $id")
      //TODO: Show floating error message
    }
  }

  override fun onAddSearchResultToQueue(id: String) {
    logger.debug("onAddSearchResultToQueue clicked")
    val video = findVideoFromLoadedVideos(id)
    if(video != null) {
      playbackQueue.add(video)
    } else {
      logger.error("Failed to find video from last loaded videos. Can't add to queue, search result with id = $id")
      //TODO: Show floating error message
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
