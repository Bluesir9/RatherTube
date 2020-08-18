package ui.central_content

import extensions.createHtmlElementWithClass
import extensions.createHtmlElementWithId
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.w3c.dom.HTMLElement
import ui.base.Renderable
import ui.central_content.CentralContentVM.*
import kotlin.browser.document

@Suppress("EXPERIMENTAL_API_USAGE")
class CentralContentViewImpl(
  override val rootElement: HTMLElement
) : Renderable(rootElement), CentralContentContract.View {

  private lateinit var progressLoaderContainer: HTMLElement
  private lateinit var progressLoaderView: CentralProgressLoaderView

  private lateinit var noSearchResultsContainer: HTMLElement
  private lateinit var noSearchResultsView: CentralNoSearchResultsView

  private lateinit var searchResultsContainer: HTMLElement
  private lateinit var searchResultsView: CentralSearchResultsView

  private val presenter : CentralContentContract.Presenter = CentralContentPresenterImpl()

  override fun initLayout() {
    progressLoaderContainer =
      document.createHtmlElementWithClass(
        localName = "div",
        clazz = "area_content_central_loader",
        applyCSS = { style ->
          style.display = "none"
          style.margin = "auto auto"
          style.width = "200px"
          style.height = "80px"
          style.textAlign = "center"
          style.fontSize = "10px"
        }
      )

    noSearchResultsContainer =
      document.createHtmlElementWithId(
        localName = "div",
        id = "area_content_central_no_search_results_container",
        applyCSS = { style ->
          style.display = "none"
          style.flex = "1"
          style.flexDirection = "column"
          style.justifyContent = "center"
          style.alignItems = "center"
        }
      )

    searchResultsContainer =
      document.createHtmlElementWithId(
        localName = "div",
        id = "area_content_content_grid_container",
        applyCSS = { style ->
          style.display = "grid"
          style.setProperty("grid-template-columns", "repeat(auto-fill, minmax(300px, 1fr)")
          style.setProperty("grid-gap", "10px")
          style.justifyContent = "center"
        }
      )

    rootElement.append(progressLoaderContainer, noSearchResultsContainer, searchResultsContainer)

    progressLoaderView = CentralProgressLoaderView(progressLoaderContainer).also { it.create() }
    noSearchResultsView = CentralNoSearchResultsView(noSearchResultsContainer).also { it.create() }
    searchResultsView = CentralSearchResultsView(searchResultsContainer).also { it.create() }

    searchResultsView.getGridItemClickEvents()
      .onEach { presenter.onSearchResultClick(it) }
      .launchIn(this)
  }

  override fun onLayoutReady() {
    presenter.start(this)
  }

  override fun render(vm: CentralContentVM) {
    when(vm) {
      is LoadingVM -> setVisibilities(progressLoaderVisible = true)
      is EmptyVM -> setVisibilities(noSearchResultsVisible = true)
      is SearchResults -> {
        setVisibilities(searchResultsVisible = true)
        searchResultsView.render(vm)
      }
      is ErrorVM -> {
        setVisibilities(errorVisible = true)
        noSearchResultsView.render(vm)
      }
    }
  }

  private fun setVisibilities(
    progressLoaderVisible: Boolean = false,
    noSearchResultsVisible: Boolean = false,
    searchResultsVisible: Boolean = false,
    errorVisible: Boolean = false) {
    if(progressLoaderVisible) {
      progressLoaderContainer.style.display = ""
    } else {
      progressLoaderContainer.style.display = "none"
    }
    if(noSearchResultsVisible || errorVisible) {
      noSearchResultsContainer.style.display = "flex"
    } else {
      noSearchResultsContainer.style.display = "none"
    }
    if(searchResultsVisible) {
      searchResultsContainer.style.display = "grid"
    } else {
      searchResultsContainer.style.display = "none"
    }
  }

  override fun onDestroy() {
    presenter.stop()
    progressLoaderView.destroy()
    noSearchResultsView.destroy()
    searchResultsView.destroy()
  }
}
