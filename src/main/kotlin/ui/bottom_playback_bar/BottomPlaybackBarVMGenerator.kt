package ui.bottom_playback_bar

import youtube.YouTubeVideo
import youtube.YouTubeVideoPlaybackEventListener.Event
import youtube.YouTubeVideoPlaybackEventListener.Event.*

interface BottomPlaybackBarVMGenerator {
  operator fun invoke(playbackEvent: Event): BottomPlaybackBarVM
}

class BottomPlaybackBarVMGeneratorImpl : BottomPlaybackBarVMGenerator {

  override fun invoke(playbackEvent: Event): BottomPlaybackBarVM =
    when (playbackEvent) {
      is Loading,
      is Loaded,
      is Buffering,
      is Playing -> getVM(video = playbackEvent.video, playButtonVisible = false, pauseButtonVisible = true)
      is Paused -> getVM(video = playbackEvent.video, playButtonVisible = true, pauseButtonVisible = false)
      /*
      FIXME:
        Load has failed so I should have a dedicated
        UI representation for the same.
      */
      is LoadFailed -> TODO()
    }

  private fun getVM(video: YouTubeVideo, playButtonVisible: Boolean, pauseButtonVisible: Boolean): BottomPlaybackBarVM =
    BottomPlaybackBarVM(
      trackTitle = video.title,
      trackArtist = video.artist,
      playButtonVisible = playButtonVisible,
      pauseButtonVisible = pauseButtonVisible
    )
}
