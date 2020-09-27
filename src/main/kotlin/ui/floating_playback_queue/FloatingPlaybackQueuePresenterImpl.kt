@file:Suppress("EXPERIMENTAL_API_USAGE")

package ui.floating_playback_queue

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import logging.Logger
import logging.LoggerImpl
import playback.queue.PlaybackQueue
import playback.queue.PlaybackQueueImpl
import playback.queue.PlaybackQueueItem
import playback.usecases.Play
import ui.base.BasePresenterImpl

class FloatingPlaybackQueuePresenterImpl :
  FloatingPlaybackQueueContract.Presenter,
  BasePresenterImpl<FloatingPlaybackQueueContract.View>() {

  private val queueButtonClickEventsProvider: QueueButtonClickEventsProvider = QueueButtonClickEventsManager
  private var vm: FloatingPlaybackQueueVM = FloatingPlaybackQueueVM()
  private val playbackQueue: PlaybackQueue = PlaybackQueueImpl
  private val generateVM: FloatingPlaybackQueueVMGenerator = FloatingPlaybackQueueVMGeneratorImpl()
  private lateinit var playbackQueueItems: List<PlaybackQueueItem>
  private val logger: Logger = LoggerImpl(FloatingPlaybackQueuePresenterImpl::class.simpleName!!)
  private val play = Play()

  override fun onStart() {
    playbackQueue.stream()
      .onEach { this.playbackQueueItems = it }
      .map { generateVM(vm, it) }
      .onEach { render(it) }
      .launchIn(this)

    queueButtonClickEventsProvider.getClickEvents()
      .map { vm.copy(visible = !vm.visible) }
      .onEach { render(it) }
      .launchIn(this)
  }

  override fun onClearQueueButtonClick() {
    playbackQueue.clear()
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
      playbackQueue.remove(playbackQueueItem)
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
