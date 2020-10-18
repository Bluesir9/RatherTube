package ui.floating_playback_queue

import ui.base.BasePresenter
import ui.base.BaseView

interface FloatingPlaybackQueueContract {
  interface View : BaseView {
    fun render(vm: FloatingPlaybackQueueVM)
  }

  interface Presenter : BasePresenter<View> {
    fun onClearQueueButtonClick()
    fun onPlayQueueItemClick(itemVM: FloatingPlaybackQueueVM.Item)
    fun onRemoveQueueItemClick(itemVM: FloatingPlaybackQueueVM.Item)
  }
}
