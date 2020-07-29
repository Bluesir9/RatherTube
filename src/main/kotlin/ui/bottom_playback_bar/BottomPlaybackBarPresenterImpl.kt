@file:Suppress("EXPERIMENTAL_API_USAGE")

package ui.bottom_playback_bar

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import song.SongPlaybackCoordinator
import song.SongPlaybackEventListener
import song.SongPlayer
import ui.base.BasePresenterImpl

class BottomPlaybackBarPresenterImpl :
  BottomPlaybackBarContract.Presenter,
  BasePresenterImpl<BottomPlaybackBarContract.View>() {

  private val songPlayer: SongPlayer = SongPlaybackCoordinator
  private val songPlaybackEventListener: SongPlaybackEventListener = SongPlaybackCoordinator
  private val generateVM: BottomPlaybackBarVMGenerator = BottomPlaybackBarVMGeneratorImpl()

  private var lastPlaybackEvent: SongPlaybackEventListener.Event? = null

  override fun onStart() {
    songPlaybackEventListener.getSongPlaybackEvents()
      .onEach { event -> lastPlaybackEvent = event }
      .map { generateVM(it) }
      .onEach { view.render(it) }
      .launchIn(this)
  }

  override fun goToPreviousTrackButtonClick() {
    songPlayer.rewindRequested()
  }

  override fun goToNextTrackButtonClick() {
    songPlayer.forwardRequested()
  }

  override fun playCurrentTrackButtonClick() {
    lastPlaybackEvent?.song?.let { songPlayer.playRequested(it) }
  }

  override fun pauseCurrentTrackButtonClick() {
    songPlayer.pauseRequested()
  }

  override fun onStop() {
    //no-op
  }
}
