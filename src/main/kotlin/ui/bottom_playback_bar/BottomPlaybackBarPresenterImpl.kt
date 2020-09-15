@file:Suppress("EXPERIMENTAL_API_USAGE")

package ui.bottom_playback_bar

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ui.base.BasePresenterImpl
import playback.player.*

class BottomPlaybackBarPresenterImpl :
  BottomPlaybackBarContract.Presenter,
  BasePresenterImpl<BottomPlaybackBarContract.View>() {

  private val player: YouTubeVideoPlayer = YouTubeVideoPlayerImpl
  private val generateVM: BottomPlaybackBarVMGenerator = BottomPlaybackBarVMGeneratorImpl()

  private var lastPlaybackEvent: YouTubeVideoPlayer.Event? = null

  override fun onStart() {
    player.getEvents()
      .onEach { event -> lastPlaybackEvent = event }
      .map { generateVM(it) }
      .onEach { view.render(it) }
      .launchIn(this)
  }

  override fun goToPreviousTrackButtonClick() {
    player.rewind()
  }

  override fun goToNextTrackButtonClick() {
    player.forward()
  }

  override fun playCurrentTrackButtonClick() {
    lastPlaybackEvent?.playbackQueueItem?.let(player::play)
  }

  override fun pauseCurrentTrackButtonClick() {
    player.pause()
  }

  override fun onStop() {
    //no-op
  }
}
