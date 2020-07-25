package ui.bottom_playback_bar

import ui.base.BasePresenter
import ui.base.BaseView

interface BottomPlaybackBarContract {
  interface View : BaseView {
    fun render(vm: BottomPlaybackBarVM)
  }

  interface Presenter : BasePresenter<View> {
    fun goToPreviousTrackButtonClick()
    fun goToNextTrackButtonClick()
    fun playCurrentTrackButtonClick()
    fun pauseCurrentTrackButtonClick()
  }
}
