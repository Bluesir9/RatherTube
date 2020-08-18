package ui.top_search_bar

import config.Color
import config.StringResource
import extensions.createHtmlElementWithId
import extensions.createHtmlInputElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import ui.base.Renderable
import kotlin.browser.document

class TopSearchBarViewImpl(
  override val rootElement: HTMLElement
): Renderable(rootElement), TopSearchBarContract.View {

  private val presenter: TopSearchBarContract.Presenter = TopSearchBarPresenterImpl()

  override fun initLayout() {
    val topBarTitle =
      document.createHtmlElementWithId(
        localName = "p",
        id = "area_top_bar_title",
        applyCSS = { style ->
          style.fontSize = "1.2rem"
          style.color = Color.PRIMARY_BLUE
        }
      ).also {
        it.innerText = StringResource.RATHERTUBE
      }
    val topBarSearchInput =
      document.createHtmlInputElement(
        type = "search",
        id = "area_top_bar_search",
        /*
        FIXME: Move "Search" into a "string
          resources" file/class/object kinda
          like Android has the strings.xml file.
        */
        placeholder = StringResource.SEARCH_BOX_PLACEHOLDER,
        applyCSS = { style ->
          style.display = "flex"
          style.flex = "1"
          style.marginLeft = "10px"
          style.background = Color.BACKGROUND_BLACK
          style.color = Color.TEXT_GREY
          style.borderColor = Color.INPUT_BOX_GREY_BORDER
          style.borderWidth = "1px"
          style.fontSize = "1.2rem"
          style.padding = "8px"
        }
      )

    rootElement.appendChild(topBarTitle)
    rootElement.appendChild(topBarSearchInput)

    topBarSearchInput.onchange = {
      val text = (it.target as HTMLInputElement).value
      presenter.onSearchQueryDecided(text)
    }
  }

  override fun onLayoutReady() {
    presenter.start(this)
  }

  override fun render(vm: TopSearchBarVM) {
    /*
    The communication for this UI element is
    mostly one-way, so there really isn't
    anything to render.
     */
  }

  override fun onDestroy() {
    presenter.stop()
  }
}
