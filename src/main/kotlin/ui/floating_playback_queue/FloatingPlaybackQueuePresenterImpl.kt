@file:Suppress("EXPERIMENTAL_API_USAGE", "MoveLambdaOutsideParentheses")

package ui.floating_playback_queue

import kotlinx.coroutines.flow.*
import logging.Logger
import logging.LoggerImpl
import playback.player.YouTubeVideoPlayer
import playback.player.YouTubeVideoPlayer.Event.WithPlayable
import playback.player.YouTubeVideoPlayer.Event.WithoutPlayable
import playback.player.YouTubeVideoPlayerImpl
import playback.queue.*
import playback.usecases.Play
import ui.base.BasePresenterImpl

class FloatingPlaybackQueuePresenterImpl :
  FloatingPlaybackQueueContract.Presenter,
  BasePresenterImpl<FloatingPlaybackQueueContract.View>() {

  private val logger: Logger = LoggerImpl(FloatingPlaybackQueuePresenterImpl::class.simpleName!!)

  private val queueButtonClickEventsProvider: QueueButtonClickEventsProvider = QueueButtonClickEventsManager
  private var vm: FloatingPlaybackQueueVM = FloatingPlaybackQueueVM()
  private val playbackQueue: PlaybackQueue = PlaybackQueueImpl
  private val generateVM: FloatingPlaybackQueueVMGenerator = FloatingPlaybackQueueVMGeneratorImpl()
  private val play = Play()
  private val clearPlaybackQueue = ClearPlaybackQueue()
  private val removeFromPlaybackQueue = RemoveFromPlaybackQueue()
  private val player: YouTubeVideoPlayer = YouTubeVideoPlayerImpl

  private lateinit var playbackQueueItems: List<PlaybackQueueItem>

  override fun onStart() {
    playbackQueue.stream()
      .onEach { this.playbackQueueItems = it }
      .combine(player.getEvents(), { queueItems, playbackEvent ->
        val currentPlaybackItem =
          when(playbackEvent) {
            is WithoutPlayable -> null
            is WithPlayable -> playbackEvent.playbackQueueItem
          }
        generateVM(vm, queueItems, currentPlaybackItem)
      })
      .onEach { render(it) }
      .launchIn(this)

    queueButtonClickEventsProvider.getClickEvents()
      .map { vm.copy(visible = !vm.visible) }
      .onEach { render(it) }
      .launchIn(this)
  }

  override fun onClearQueueButtonClick() {
    clearPlaybackQueue()
  }

  override fun onPlayQueueItemClick(itemVM: FloatingPlaybackQueueVM.Item) {
    val playbackQueueItem = getMatchingPlaybackQueueItem(itemVM)
    if(playbackQueueItem != null) {
      play(playbackQueueItem)
    } else {
      logger.error("Failed to find playback queue item clicked in playback queue, so cant play it. " +
        "Item VM = $itemVM, playbackQueueItems = $playbackQueueItems")
      //TODO: Show floating error message
    }
  }

  override fun onRemoveQueueItemClick(itemVM: FloatingPlaybackQueueVM.Item) {
    val playbackQueueItem = getMatchingPlaybackQueueItem(itemVM)
    if(playbackQueueItem != null) {
      removeFromPlaybackQueue(playbackQueueItem)
    } else {
      logger.error("Failed to find playback queue item clicked in playback queue, so cant remove it. " +
        "Item VM = $itemVM, playbackQueueItems = $playbackQueueItems")
      //TODO: Show floating error message
    }
  }

  override fun onStop() {
    //no-op
  }

  private fun render(vm: FloatingPlaybackQueueVM) {
    this.vm = vm
    view.render(vm)
  }

  private fun getMatchingPlaybackQueueItem(itemVM: FloatingPlaybackQueueVM.Item): PlaybackQueueItem? =
    playbackQueueItems.firstOrNull { it.id == itemVM.id }
}
