package ui.bottom_playback_bar

import config.Color
import extensions.createIcon
import org.w3c.dom.HTMLElement
import ui.base.Renderable
import kotlin.browser.document

class BottomQueueButtonView(
  override val rootElement: HTMLElement
) : Renderable(rootElement) {

  override fun initLayout() {
    val queueButtonIcon = document.createIcon(
      clazz = "fas fa-list",
      id = "icon_bottom_play_menu_track_queue_button",
      applyCSS = { style ->
        /*
        FIXME:
          The queue functionality has not been implemented
          as of now, so the button will be hidden.
         */
        style.display = "none"
        style.fontSize = "25px"
        style.color = Color.PRIMARY_BLUE
      }
    )

    rootElement.appendChild(queueButtonIcon)
  }

  override fun onLayoutReady() {
    //no-op
  }

  override fun onDestroy() {
    //no-op
  }
}
