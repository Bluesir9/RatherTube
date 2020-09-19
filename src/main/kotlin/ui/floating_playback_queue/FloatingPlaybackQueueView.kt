package ui.floating_playback_queue

import config.Color
import extensions.createHtmlElementWithId
import org.w3c.dom.HTMLElement
import ui.base.Renderable
import kotlin.browser.document

class FloatingPlaybackQueueView(
  override val rootElement: HTMLElement
) : Renderable(rootElement), FloatingPlaybackQueueContract.View {

  private lateinit var container: HTMLElement

  private val presenter : FloatingPlaybackQueueContract.Presenter = FloatingPlaybackQueuePresenterImpl()

  override fun initLayout() {
    container = document.createHtmlElementWithId(
      localName = "div",
      id = "area_bottom_play_menu_floating_playback_queue_container",
      applyCSS = { style ->
        style.position = "absolute"
        style.width = "50vw"
        style.height = "50vh"
        style.bottom = "110%"
        style.right = "10px"
        style.zIndex = "2"
        style.boxShadow = "0 0 5px ${Color.BACKGROUND_SHADOW_GRADIENT_LIGHT}, -5px -5px 5px ${Color.BACKGROUND_SHADOW_GRADIENT_DARK}"
        style.background = Color.BACKGROUND_BLACK
      }
    ).also {
      /*
      The menu will start off as hidden and
      will become visible depending on user
      interaction.
       */
      it.hidden = true
      rootElement.appendChild(it)
    }
  }

  override fun onLayoutReady() {
    presenter.start(this)
  }

  override fun render(vm: FloatingPlaybackQueueVM) {
    container.hidden = !vm.visible
  }

  override fun onDestroy() {
    //TODO("Not yet implemented")
  }
}
