@file:Suppress("EXPERIMENTAL_API_USAGE")

package playback.player

import com.soywiz.klock.seconds
import config.StringResource
import extensions.playedLength
import extensions.totalLength
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import logging.Logger
import logging.LoggerImpl
import org.w3c.dom.Audio
import playback.player.YouTubeVideoPlayer.Event
import playback.queue.PlaybackQueue
import playback.queue.PlaybackQueueImpl
import playback.queue.PlaybackQueueItem
import ui.floating_message.ShowFloatingMessage
import ui.floating_message.ShowFloatingMessageImpl
import utils.RepoResult
import utils.RepoResult.Failure
import utils.RepoResult.Success
import youtube.YouTubeVideoRepository
import youtube.YouTubeVideoRepositoryImpl

object YouTubeVideoPlayerImpl: YouTubeVideoPlayer, CoroutineScope by CoroutineScope(Dispatchers.Main) {

  private data class MediaFile(val playbackQueueItem: PlaybackQueueItem, val platformAudio: Audio) {
    fun play() {
      this.platformAudio.play()
    }

    fun pause() {
      this.platformAudio.pause()
    }

    fun stop() {
      this.platformAudio.src = ""
    }

    fun rewind() {
      this.platformAudio.fastSeek(0.0)
      this.platformAudio.play()
    }
  }

  private val eventChannel = BroadcastChannel<Event>(1)
  private val youTubeVideoRepo: YouTubeVideoRepository = YouTubeVideoRepositoryImpl
  private var activeMediaFile: MediaFile? = null
  private val logger: Logger = LoggerImpl(YouTubeVideoPlayerImpl::class.simpleName!!)
  private val playbackQueue: PlaybackQueue = PlaybackQueueImpl
  private val showFloatingMessage: ShowFloatingMessage = ShowFloatingMessageImpl

  init {
    broadcastEvent(Event.WithoutPlayable.Cleared)
  }

  //region override
  override fun play(playbackQueueItem: PlaybackQueueItem) {
    val activeMediaFileCopy = activeMediaFile
    if (activeMediaFileCopy != null && activeMediaFileCopy.playbackQueueItem.video == playbackQueueItem.video) {
      /*
      Request made to play video that had been played last.
      So start playing that video again.
      */
      activeMediaFileCopy.play()
    } else {
      /*
      Attempting to play an item different the one currently available inside the player.
      So clear the available item and make way for new item.
      */
      clearActiveMediaFile()

      launch {
        when (val streamUrlLoadResult = getStreamUrl(playbackQueueItem)) {
          is Success -> {
            val newAudio = Audio(streamUrlLoadResult.value)
            val newMediaFile = MediaFile(playbackQueueItem, newAudio)
            setupMediaFileEventListeners(newMediaFile)
            activeMediaFile = newMediaFile
            newMediaFile.play()
          }
          is Failure -> showFloatingMessage(StringResource.STREAM_LOAD_FAILED)
        }
      }
    }
  }

  override fun pause() {
    val activeMediaFileCopy = activeMediaFile
    if (activeMediaFileCopy != null) {
      activeMediaFileCopy.pause()
    } else {
      logger.error("Attempting to pause player when activeMediaFile is null")
      showFloatingMessage(StringResource.PAUSE_FAILED_CAUSE_NO_TRACK_FOUND)
    }
  }

  override fun resume() {
    val activeMediaFileCopy = activeMediaFile
    if (activeMediaFileCopy != null) {
      activeMediaFileCopy.play()
    } else {
      logger.error("Attempting to resume player when activeMediaFile is null")
      showFloatingMessage(StringResource.RESUME_FAILED_CAUSE_NO_TRACK_FOUND)
    }
  }

  override fun rewind() {
    val activeMediaFileCopy = activeMediaFile
    if (activeMediaFileCopy != null) {
      val playedLength = activeMediaFileCopy.platformAudio.playedLength
      /*
      If more than 10 seconds have been played of the current active
      media file, then rewind the current track to the very beginning.

      If less than 10 seconds have been played of the current active
      media file, then load the previous track in the playback queue
       */
      if (playedLength >= 10.seconds) {
        activeMediaFileCopy.rewind()
      } else {
        val playbackQueueItems = playbackQueue.get()
        val indexOfActivePlaybackItem = playbackQueueItems.indexOf(activeMediaFileCopy.playbackQueueItem)
        if (indexOfActivePlaybackItem > 0) {
          val newPlaybackItem = playbackQueueItems[indexOfActivePlaybackItem - 1]
          play(newPlaybackItem)
        } else {
          /*
          Either this is the first item in the playback queue,
          Or this item is not present in the playback queue.

          Either way we cant rewind any further so will simply
          seek to the beginning of this track.
          */
          activeMediaFileCopy.rewind()

          /*
          We failed to find the activeMediaFileCopy.playbackQueueItem in the playback
          queue, so lets log an error for the same.
          */
          if (indexOfActivePlaybackItem == -1) {
            logger.error(
              "Failed to find active playback item in the playback queue while rewinding. \n" +
                "activeMediaFileCopy = $activeMediaFileCopy \n" +
                "playbackQueueItems = $playbackQueueItems"
            )
            showFloatingMessage(StringResource.REWIND_FAILED_CAUSE_ACTIVE_TRACK_NOT_FOUND_IN_QUEUE)
          }
        }
      }
    } else {
      logger.error("Attempting to rewind player when activeMediaFile is null")
      showFloatingMessage(StringResource.REWIND_FAILED_CAUSE_NO_ACTIVE_TRACK_FOUND)
    }
  }

  override fun forward() {
    val activeMediaFileCopy = activeMediaFile
    if(activeMediaFileCopy == null) {
      logger.error(
        "Attempting to go to next track when no existing track was found. Will begin playing " +
          "first track of the playback queue."
      )
      showFloatingMessage(StringResource.FORWARD_FAILED_CAUSE_NO_ACTIVE_TRACK_FOUND)
      return
    }
    val playbackQueueItems = playbackQueue.get()
    val indexOfActivePlaybackItem = playbackQueueItems.indexOf(activeMediaFileCopy.playbackQueueItem)
    /*
    We failed to find the activeMediaFileCopy.playbackQueueItem in the playback
    queue, so lets log an error for the same and show an error message.
    */
    if (indexOfActivePlaybackItem == -1) {
      logger.error(
        "Failed to find active playback item in the playback queue while forwarding. \n" +
          "activeMediaFileCopy = $activeMediaFileCopy \n" +
          "playbackQueueItems = $playbackQueueItems"
      )
      showFloatingMessage(StringResource.FORWARD_FAILED_CAUSE_ACTIVE_TRACK_NOT_FOUND_IN_QUEUE)
      return
    }

    val indexOfNewPlaybackItem = indexOfActivePlaybackItem + 1

    if(indexOfNewPlaybackItem < playbackQueueItems.size) {
      val newPlaybackItem = playbackQueueItems[indexOfNewPlaybackItem]
      play(newPlaybackItem)
    } else {
      showFloatingMessage(StringResource.FORWARD_FAILED_CAUSE_NO_TRACKS_AFTER_CURRENT)
    }
  }

  override fun getCurrentlyPlayingItem(): PlaybackQueueItem? = activeMediaFile?.playbackQueueItem

  override fun clear() {
    clearActiveMediaFile()
    broadcastEvent(Event.WithoutPlayable.Cleared)
  }

  override fun seekTo(percentage: Double) {
    val activeMediaFileCopy = activeMediaFile
    if(activeMediaFileCopy != null) {
      val totalLength = activeMediaFileCopy.platformAudio.totalLength
      val seekToLengthInSeconds = (percentage / 100) * totalLength.seconds
      activeMediaFileCopy.platformAudio.fastSeek(seekToLengthInSeconds)
      activeMediaFileCopy.platformAudio.play()
    } else {
      logger.error("No active media file found so can't seek", NullPointerException("No active media file found so can't seek"))
      showFloatingMessage(StringResource.FAILED_TO_SEEK_CAUSE_NO_ACTIVE_TRACK_FOUND)
    }
  }

  override fun getEvents(): Flow<Event> = eventChannel.asFlow()
  //endregion

  //region private
  private suspend fun getStreamUrl(playbackQueueItem: PlaybackQueueItem): RepoResult<String> {
    broadcastEvent(Event.WithPlayable.Loading(playbackQueueItem))
    val repoResult = youTubeVideoRepo.getStreamUrl(playbackQueueItem.video)
    val eventToBroadcast = when (repoResult) {
      is Success -> Event.WithPlayable.Loaded(playbackQueueItem)
      is Failure -> Event.WithPlayable.LoadFailed(playbackQueueItem, repoResult.message)
    }
    broadcastEvent(eventToBroadcast)
    return repoResult
  }

  private fun broadcastEvent(event: Event) {
    eventChannel.offer(event)
  }

  private fun setupMediaFileEventListeners(mediaFile: MediaFile) {
    mediaFile.platformAudio.ontimeupdate = {
      broadcastEvent(
        Event.WithPlayable.Playing(
          playbackQueueItem = mediaFile.playbackQueueItem,
          playedLength = mediaFile.platformAudio.playedLength,
          totalLength = mediaFile.platformAudio.totalLength
        )
      )
    }

    mediaFile.platformAudio.onpause = {
      broadcastEvent(
        Event.WithPlayable.Paused(
          playbackQueueItem = mediaFile.playbackQueueItem,
          playedLength = mediaFile.platformAudio.playedLength,
          totalLength = mediaFile.platformAudio.totalLength
        )
      )
    }

    mediaFile.platformAudio.onended = { forward() }
  }

  private fun clearActiveMediaFile() {
    val activeMediaFileCopy = activeMediaFile
    if(activeMediaFileCopy != null) {
      clearMediaFileEventListeners(activeMediaFileCopy)
      activeMediaFileCopy.stop()
      activeMediaFile = null
    }
  }

  private fun clearMediaFileEventListeners(mediaFile: MediaFile) {
    mediaFile.platformAudio.ontimeupdate = null
    mediaFile.platformAudio.onpause = null
  }
  //endregion
}
