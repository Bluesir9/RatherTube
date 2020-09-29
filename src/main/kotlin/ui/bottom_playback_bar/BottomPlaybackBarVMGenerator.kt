package ui.bottom_playback_bar

import youtube.YouTubeVideo
import playback.player.YouTubeVideoPlayer.Event
import playback.player.YouTubeVideoPlayer.Event.*
import playback.player.YouTubeVideoPlayer.Event.WithPlayable.*

interface BottomPlaybackBarVMGenerator {
  operator fun invoke(playbackEvent: Event): BottomPlaybackBarVM
}

class BottomPlaybackBarVMGeneratorImpl : BottomPlaybackBarVMGenerator {

  override fun invoke(playbackEvent: Event): BottomPlaybackBarVM =
    when (playbackEvent) {
      is WithoutPlayable -> getWithoutPlayableVM(playbackEvent)
      is WithPlayable -> getWithPlayableVM(playbackEvent)
    }

  private fun getWithoutPlayableVM(event: WithoutPlayable): BottomPlaybackBarVM =
    when (event) {
      is WithoutPlayable.Cleared -> BottomPlaybackBarVM(
        trackTitle = "",
        trackArtist = "",
        playButtonVisible = true,
        pauseButtonVisible = false
      )
    }

  private fun getWithPlayableVM(event: WithPlayable): BottomPlaybackBarVM =
    when (event) {
      is Loading,
      is Loaded,
      is Buffering,
      is Playing -> getVM(
        video = event.playbackQueueItem.video,
        playButtonVisible = false,
        pauseButtonVisible = true
      )
      is Paused -> getVM(
        video = event.playbackQueueItem.video,
        playButtonVisible = true,
        pauseButtonVisible = false
      )
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
