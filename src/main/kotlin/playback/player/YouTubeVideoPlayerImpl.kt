@file:Suppress("EXPERIMENTAL_API_USAGE")

package playback.player

import com.soywiz.klock.seconds
import config.StringResource
import extensions.playedLength
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
    }
  }

  private val eventChannel = BroadcastChannel<Event>(1)
  private val youTubeVideoRepo: YouTubeVideoRepository = YouTubeVideoRepositoryImpl
  private var activeMediaFile: MediaFile? = null
  private val logger: Logger = LoggerImpl(YouTubeVideoPlayerImpl::class.simpleName!!)
  private val playbackQueue: PlaybackQueue = PlaybackQueueImpl

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
      activeMediaFileCopy?.stop()

      launch {
        when (val streamUrlLoadResult = getStreamUrl(playbackQueueItem)) {
          is Success -> {
            val newAudio = Audio(streamUrlLoadResult.value)
            val newMediaFile = MediaFile(playbackQueueItem, newAudio)
            setupMediaFileEventListeners(newMediaFile)
            activeMediaFile = newMediaFile
            newMediaFile.play()
          }
          is Failure -> showFloatingErrorMessage(StringResource.STREAM_LOAD_FAILED)
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
      showFloatingErrorMessage(StringResource.PAUSE_FAILED_CAUSE_NO_TRACK_FOUND)
    }
  }

  override fun resume() {
    val activeMediaFileCopy = activeMediaFile
    if (activeMediaFileCopy != null) {
      activeMediaFileCopy.play()
    } else {
      logger.error("Attempting to resume player when activeMediaFile is null")
      showFloatingErrorMessage(StringResource.RESUME_FAILED_CAUSE_NO_TRACK_FOUND)
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
          }
        }
      }
    } else {
      logger.error("Attempting to rewind player when activeMediaFile is null")
      showFloatingErrorMessage(StringResource.REWIND_FAILED_CAUSE_NO_TRACK_FOUND)
    }
  }

  override fun forward() {
    val activeMediaFileCopy = activeMediaFile
    val playbackQueueItems = playbackQueue.get()
    val indexOfNewPlaybackItem =
      if (activeMediaFileCopy != null) {
        val indexOfActivePlaybackItem = playbackQueueItems.indexOf(activeMediaFileCopy.playbackQueueItem)
        /*
        We failed to find the activeMediaFileCopy.playbackQueueItem in the playback
        queue, so lets log an error for the same.
        */
        if (indexOfActivePlaybackItem == -1) {
          logger.error(
            "Failed to find active playback item in the playback queue while forwarding. \n" +
              "activeMediaFileCopy = $activeMediaFileCopy \n" +
              "playbackQueueItems = $playbackQueueItems"
          )
        }

        indexOfActivePlaybackItem + 1
      } else {
        logger.error(
          "Attempting to go to next track when no existing track was found. Will begin playing +" +
          "first track of the playback queue."
        )
        /*
        We don't want the user to be stuck here in this state. So
        we will go fetch the first item in the playback queue and
        play it.
         */
        0
      }

    val newPlaybackItem = playbackQueueItems[indexOfNewPlaybackItem]
    play(newPlaybackItem)
  }

  override fun getEvents(): Flow<Event> = eventChannel.asFlow()
  //endregion

  //region private
  private suspend fun getStreamUrl(playbackQueueItem: PlaybackQueueItem): RepoResult<String> {
    broadcastEvent(Event.Loading(playbackQueueItem))
    val repoResult = youTubeVideoRepo.getStreamUrl(playbackQueueItem.video)
    val eventToBroadcast = when (repoResult) {
      is Success -> Event.Loaded(playbackQueueItem)
      is Failure -> Event.LoadFailed(playbackQueueItem, repoResult.message)
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
        Event.Playing(
          playbackQueueItem = mediaFile.playbackQueueItem,
          playedLength = mediaFile.platformAudio.playedLength
        )
      )
    }

    mediaFile.platformAudio.onpause = {
      broadcastEvent(
        Event.Paused(
          playbackQueueItem = mediaFile.playbackQueueItem,
          playedLength = mediaFile.platformAudio.playedLength
        )
      )
    }
  }

  private fun showFloatingErrorMessage(message: String) {
    //TODO: everything
  }
  //endregion
}
