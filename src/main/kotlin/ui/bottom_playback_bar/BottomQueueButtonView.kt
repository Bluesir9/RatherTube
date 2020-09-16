package ui.bottom_playback_bar

import config.Color
import extensions.clickEventsAsFlow
import extensions.createIcon
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.HTMLElement
import ui.base.Renderable
import kotlin.browser.document

class BottomQueueButtonView(
  override val rootElement: HTMLElement
) : Renderable(rootElement) {

  private lateinit var queueButtonIcon: HTMLElement

  override fun initLayout() {
    queueButtonIcon = makeQueueButton().also { rootElement.appendChild(it) }
  }

  private fun makeQueueButton(): HTMLElement =
    document.createIcon(
      clazz = "fas fa-list",
      id = "icon_bottom_play_menu_track_queue_button",
      applyCSS = { style ->
        style.fontSize = "25px"
        style.color = Color.PRIMARY_GREEN
      }
    )

  override fun onLayoutReady() {
    //no-op
  }

  fun getQueueButtonClickEvents(): Flow<Unit> = queueButtonIcon.clickEventsAsFlow()

  override fun onDestroy() {
    //no-op
  }
}
