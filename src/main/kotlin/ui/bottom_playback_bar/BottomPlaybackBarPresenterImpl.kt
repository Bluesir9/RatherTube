@file:Suppress("EXPERIMENTAL_API_USAGE")

package ui.bottom_playback_bar

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ui.base.BasePresenterImpl
import youtube.YouTubeVideo
import youtube.YouTubeVideoPlaybackCoordinator
import youtube.YouTubeVideoPlaybackEventListener
import youtube.YouTubeVideoPlaybackEventListener.Event
import youtube.YouTubeVideoPlayer

class BottomPlaybackBarPresenterImpl :
  BottomPlaybackBarContract.Presenter,
  BasePresenterImpl<BottomPlaybackBarContract.View>() {

  private val videoPlayer: YouTubeVideoPlayer = YouTubeVideoPlaybackCoordinator
  private val videoPlaybackEventListener: YouTubeVideoPlaybackEventListener = YouTubeVideoPlaybackCoordinator
  private val generateVM: BottomPlaybackBarVMGenerator = BottomPlaybackBarVMGeneratorImpl()

  private var lastPlaybackEvent: Event? = null

  override fun onStart() {
    videoPlaybackEventListener.getSongPlaybackEvents()
      .onEach { event -> lastPlaybackEvent = event }
      .map { generateVM(it) }
      .onEach { view.render(it) }
      .launchIn(this)
  }

  override fun goToPreviousTrackButtonClick() {
    videoPlayer.rewindRequested()
  }

  override fun goToNextTrackButtonClick() {
    videoPlayer.forwardRequested()
  }

  override fun playCurrentTrackButtonClick() {
    lastPlaybackEvent?.video?.let { playVideo(it) }
  }

  private fun playVideo(video: YouTubeVideo) {
    launch {
      videoPlayer.playRequested(video)
    }
  }

  override fun pauseCurrentTrackButtonClick() {
    videoPlayer.pauseRequested()
  }

  override fun onStop() {
    //no-op
  }
}
