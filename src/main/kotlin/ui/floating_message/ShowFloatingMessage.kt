package ui.floating_message

import config.Color
import extensions.*
import kotlinx.coroutines.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.MouseEvent
import kotlin.browser.document

interface ShowFloatingMessage {
  operator fun invoke(message: String)
}

object ShowFloatingMessageImpl : ShowFloatingMessage, CoroutineScope by CoroutineScope(Dispatchers.Main) {

  private const val FLOATING_MESSAGE_VISIBLE_MILLISECONDS: Long = 2000

  private val container: HTMLElement by lazy {
    val element =
      document.createHtmlElementWithId(
        localName = "div",
        id = "area_content_floating_message_container",
        applyCSS = { style ->
          style.position = "absolute"
          style.display = "flex"
          style.flexDirection = "row"
          style.alignItems = "center"
          style.right = "10px"
          style.top = "110%"
          style.background = Color.PRIMARY_GREEN
          style.color = Color.FLOATING_MESSAGE_TEXT
          style.borderRadius = "4px"
          style.padding = "10px"
          style.zIndex = "3"
          style.boxShadow = "0 0 5px ${Color.BACKGROUND_SHADOW_GRADIENT_LIGHT}, -5px -5px 5px ${Color.BACKGROUND_SHADOW_GRADIENT_DARK}"
        }
      )
    val root = document.getHTMLElementById("area_top_bar")
    root.appendChild(element)
    element
  }

  private val messageBox: HTMLElement by lazy {
    val element = document.createHtmlElementWithId(
      localName = "div",
      id = "area_content_floating_message_content",
      applyCSS = { style ->
        style.marginRight = "10px"
        style.fontSize = "1.0rem"
      }
    )

    container.appendChild(element)
    element
  }

  private val crossButton: HTMLElement by lazy {
    val element = document.createIcon(
      clazz = "fa fa-times",
      id = "area_content_floating_message_cross",
      applyIconCss = { style ->
        style.fontSize = "20px"
      }
    )
    container.appendChild(element)
    element
  }

  private var messageRenderDurationJob: Job? = null
  private val crossButtonClickListener: (MouseEvent) -> dynamic = {
    container.hide()
  }

  override fun invoke(message: String) {
    maybeClearOldMessage()
    showMessage(message)
    messageRenderDurationJob = launch {
      waitForRenderDurationExpiry()
      hideMessage()
      messageRenderDurationJob = null
    }
  }

  private fun maybeClearOldMessage() {
    messageRenderDurationJob?.let {
      it.cancel()
      messageRenderDurationJob = null
    }
    container.hide()
  }

  private fun showMessage(message: String) {
    messageBox.innerText = message
    container.show(StyleDisplay.Flex)
    crossButton.onclick = crossButtonClickListener

  }

  private suspend fun waitForRenderDurationExpiry() {
    delay(FLOATING_MESSAGE_VISIBLE_MILLISECONDS)
  }

  private fun hideMessage() {
    crossButton.onclick = null
    container.hide()
  }
}