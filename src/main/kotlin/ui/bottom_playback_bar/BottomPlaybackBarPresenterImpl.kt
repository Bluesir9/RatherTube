@file:Suppress("EXPERIMENTAL_API_USAGE")

package ui.bottom_playback_bar

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import logging.Logger
import logging.LoggerImpl
import ui.base.BasePresenterImpl
import playback.player.*
import ui.floating_playback_queue.QueueButtonClickEventsConsumer
import ui.floating_playback_queue.QueueButtonClickEventsManager

class BottomPlaybackBarPresenterImpl :
  BottomPlaybackBarContract.Presenter,
  BasePresenterImpl<BottomPlaybackBarContract.View>() {

  private val logger: Logger = LoggerImpl(BottomPlaybackBarPresenterImpl::class.simpleName!!)
  private val player: YouTubeVideoPlayer = YouTubeVideoPlayerImpl
  private val generateVM: BottomPlaybackBarVMGenerator = BottomPlaybackBarVMGeneratorImpl()
  private var lastPlaybackEvent: YouTubeVideoPlayer.Event? = null
  private val queueButtonClickEventsConsumer: QueueButtonClickEventsConsumer = QueueButtonClickEventsManager

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
    lastPlaybackEvent?.let {
      if(it is YouTubeVideoPlayer.Event.WithPlayable) player.play(it.playbackQueueItem)
    }
  }

  override fun pauseCurrentTrackButtonClick() {
    player.pause()
  }

  override fun onQueueButtonClick() {
    queueButtonClickEventsConsumer.onQueueButtonClicked()
  }

  override fun onProgressBarClicked(clickPositionFromLeft: Int, barWidth: Int) {
    val positionAsPercentage = (clickPositionFromLeft.toDouble() / barWidth) * 100
    player.seekTo(positionAsPercentage)
  }

  override fun onStop() {
    //no-op
  }
}
