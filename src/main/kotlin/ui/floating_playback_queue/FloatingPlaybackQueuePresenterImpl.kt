@file:Suppress("EXPERIMENTAL_API_USAGE")

package ui.floating_playback_queue

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ui.base.BasePresenterImpl

class FloatingPlaybackQueuePresenterImpl :
  FloatingPlaybackQueueContract.Presenter,
  BasePresenterImpl<FloatingPlaybackQueueContract.View>() {

  private val queueButtonClickEventsProvider: QueueButtonClickEventsProvider = QueueButtonClickEventsManager
  private var vm: FloatingPlaybackQueueVM = FloatingPlaybackQueueVM(visible = false)

  override fun onStart() {
    queueButtonClickEventsProvider.getClickEvents()
      .map { vm.copy(visible = !vm.visible) }
      .onEach { this.vm = it }
      .onEach { view.render(it) }
      .launchIn(this)
  }

  override fun onStop() {
    //no-op
  }
}
