package ui.central_content

import extensions.createHtmlElementWithId
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLParagraphElement
import ui.base.Renderable
import kotlin.browser.document

class CentralNoSearchResultsView(
  override val rootElement: HTMLElement
) : Renderable(rootElement) {

  override fun initLayout() {
    val messageText =
      document.createHtmlElementWithId(
        localName = "p",
        id = "area_content_central_no_search_results_container_message",
        applyCSS = { style ->
          style.fontSize = "20px"
          style.color = "mediumseagreen"
        }
      ) as HTMLParagraphElement
    /*
    FIXME: Move below string into a "string
      resources" file/class/object kinda
      like Android has the strings.xml file.
    */
    messageText.innerHTML = "No search results found"

    rootElement.appendChild(messageText)
  }

  override fun onLayoutReady() {
    //no-op
  }

  override fun onDestroy() {
    //no-op
  }
}
