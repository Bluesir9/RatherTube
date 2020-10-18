package ui.bottom_playback_bar

import com.soywiz.klock.TimeSpan
import extensions.getAsPlaybackTimestamp
import playback.player.YouTubeVideoPlayer.Event
import playback.player.YouTubeVideoPlayer.Event.*
import playback.player.YouTubeVideoPlayer.Event.WithPlayable.*
import ui.bottom_playback_bar.BottomPlaybackBarVM.ProgressBarVM

interface BottomPlaybackBarVMGenerator {
  operator fun invoke(playbackEvent: Event): BottomPlaybackBarVM
  }

/*
FIXME:
  This implementation has looks ugly now. Rewrite it and
  add comments wherever necessary.
 */
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
        pauseButtonVisible = false,
        progressBarVM = ProgressBarVM.Hidden
      )
    }

  private fun getWithPlayableVM(event: WithPlayable): BottomPlaybackBarVM =
    when (event) {
      is Loading,
      is Loaded,
      is Buffering,
      is Playing -> getWithPlayableVM(
        event = event,
        playButtonVisible = false,
        pauseButtonVisible = true
      )
      is Paused -> getWithPlayableVM(
        event = event,
        playButtonVisible = true,
        pauseButtonVisible = false
      )
      /*
      FIXME:
        Let's have a dedicated UI representation
        for this instead of using the paused state
        UI representation.
      */
      is LoadFailed -> getWithPlayableVM(
        event = event,
        playButtonVisible = true,
        pauseButtonVisible = false
      )
    }

  private fun getWithPlayableVM(
    event: WithPlayable,
    playButtonVisible: Boolean,
    pauseButtonVisible: Boolean
  ): BottomPlaybackBarVM =
    BottomPlaybackBarVM(
      trackTitle = event.playbackQueueItem.video.title,
      trackArtist = event.playbackQueueItem.video.artist,
      playButtonVisible = playButtonVisible,
      pauseButtonVisible = pauseButtonVisible,
      progressBarVM = getProgressBarVM(event)
    )

  private fun getProgressBarVM(event: WithPlayable): ProgressBarVM {
    val playedAndTotalLengthPair =
      when (event) {
        is Loading,
        is Loaded,
        is LoadFailed -> null
        is Buffering -> Pair(event.playedLength, event.totalLength)
        is Playing -> Pair(event.playedLength, event.totalLength)
        is Paused -> Pair(event.playedLength, event.totalLength)
      }

    return if(playedAndTotalLengthPair != null) {
      ProgressBarVM.Visible(
        progressPercentage = getProgressPercentage(playedAndTotalLengthPair.first, playedAndTotalLengthPair.second),
        progressTimestamp = getProgressTimestamp(playedAndTotalLengthPair.first, playedAndTotalLengthPair.second)
      )
    } else {
      ProgressBarVM.Hidden
    }
  }

  private fun getProgressPercentage(playedLength: TimeSpan, totalLength: TimeSpan): Double =
    (playedLength.milliseconds / totalLength.milliseconds) * 100

  private fun getProgressTimestamp(playedLength: TimeSpan, totalLength: TimeSpan): String =
    "${playedLength.getAsPlaybackTimestamp()} / ${totalLength.getAsPlaybackTimestamp()}"
}
