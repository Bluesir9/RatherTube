package ui.body

import extensions.createHtmlElementWithId
import ui.base.Renderable
import ui.bottom_playback_bar.BottomPlaybackBarViewImpl
import ui.central_content.CentralContentViewImpl
import ui.top_search_bar.TopSearchBarViewImpl
import kotlin.browser.document

class Body : Renderable(document.body!!) {

  private lateinit var topSearchBarView: TopSearchBarViewImpl
  private lateinit var centralContentView: CentralContentViewImpl
  private lateinit var bottomPlaybackBarView: BottomPlaybackBarViewImpl

  override fun initLayout() {
    applyCSS()

    val topContainer =
      document.createHtmlElementWithId(
        localName = "div",
        id = "area_top_bar",
        applyCSS = { style ->
          style.display = "flex"
          style.flex = "0"
          style.flexDirection = "row"
          style.paddingLeft = "10px"
          style.paddingRight = "10px"
          style.alignItems = "baseline"
          style.boxShadow = "0 0 5px #202020, 0 2px 5px #101010"
        }
      )
    val centralContainer =
      document.createHtmlElementWithId(
        localName = "div",
        id = "area_content",
        applyCSS = { style ->
          style.display = "flex"
          style.flexDirection = "column"
          style.flex = "1"
          style.overflowY = "scroll"
          style.overflowX = "hidden"
          style.setProperty("scrollbar-color", "mediumseagreen #202020")
        }
      )
    val bottomContainer =
      document.createHtmlElementWithId(
        localName = "div",
        id = "area_bottom_play_menu",
        applyCSS = { style ->
          style.display = "flex"
          style.flex = "0"
          style.flexDirection = "row"
          style.justifyContent = "space-between"
          style.alignItems = "center"
          style.padding = "10px"
          style.boxShadow = "0 0 5px #202020, 0 -2px 5px #101010"
        }
      )

    rootElement.append(topContainer, centralContainer, bottomContainer)

    topSearchBarView = TopSearchBarViewImpl(topContainer).also { it.create() }
    centralContentView = CentralContentViewImpl(centralContainer).also { it.create() }
    bottomPlaybackBarView = BottomPlaybackBarViewImpl(bottomContainer).also { it.create() }
  }

  private fun applyCSS() {
    rootElement.style.apply {
      display = "flex"
      flex = "1"
      flexDirection = "column"
      height = "100vh"
      margin = "0"
      fontFamily = "monospace"
      background = "#202020"
      color = "#b3b9c5"
    }
  }

  override fun onLayoutReady() {
    //no-op
  }

  override fun onDestroy() {
    //FIXME: Does the order in which they are called matter?
    bottomPlaybackBarView.destroy()
    centralContentView.destroy()
    topSearchBarView.destroy()
  }
}
