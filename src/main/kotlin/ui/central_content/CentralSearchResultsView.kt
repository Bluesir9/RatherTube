package ui.central_content

import config.Color
import extensions.createHtmlElementWithClass
import extensions.getFirstHTMLElementByClassName
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.w3c.dom.*
import ui.base.Renderable
import ui.central_content.CentralContentVM.SearchResults
import ui.central_content.CentralContentVM.SearchResults.Item
import kotlin.browser.document
import kotlin.dom.clear

@Suppress("EXPERIMENTAL_API_USAGE")
class CentralSearchResultsView(
  override val rootElement: HTMLElement
) : Renderable(rootElement) {

  sealed class ClickEvent(open val searchResultId: String) {
    data class PlaySearchResult(override val searchResultId: String): ClickEvent(searchResultId)
    data class AddSearchResultToQueue(override val searchResultId: String): ClickEvent(searchResultId)
  }

  private val gridItemClickEvents = BroadcastChannel<ClickEvent>(1)

  override fun initLayout() {
    val styleElement = document.createElement("style") as HTMLStyleElement
    styleElement.innerText = getGridItemCSS()
    document.head!!.appendChild(styleElement)
  }

  override fun onLayoutReady() {
    //no-op
  }

  fun render(vm: SearchResults) {
    rootElement.clear() //FIXME: How expensive is this?
    vm.items.forEach { renderItem(it) }
  }

  fun getGridItemClickEvents(): Flow<ClickEvent> = gridItemClickEvents.asFlow()

  private fun renderItem(vm: Item) {
    val container = document.createHtmlElementWithClass(
      localName = "div",
      clazz = "area_content_content_grid_item",
      applyCSS = null
    )

    container.innerHTML = """
        <div class="area_content_content_grid_item_image_container">
          <img class="area_content_content_grid_item_image" src="${vm.trackImageUrl}"/>
          <div class="area_content_content_grid_item_hover_play_container">
            <svg class="far fa-play-circle icon_area_content_content_grid_item_hover_play"></svg>
          </div>
        </div>
        <div class="area_content_content_grid_item_track_info_container">
          <div class="area_content_content_grid_item_track_title_and_artist_container">
            <p class="area_content_content_grid_item_track_title">${vm.trackTitle}</p>
            <p class="area_content_content_grid_item_artist_name">${vm.trackArtist}</p>
          </div>
          <div class="icon_content_content_grid_item_add_to_queue_container">
            <svg class="fa fa-plus-circle icon_content_content_grid_item_add_to_queue"></svg>
          </div>
        </div>
    """.trimIndent()

    container.getFirstHTMLElementByClassName("area_content_content_grid_item_image_container").onclick = {
      gridItemClickEvents.offer(ClickEvent.PlaySearchResult(vm.id))
    }

    container.getFirstHTMLElementByClassName("icon_content_content_grid_item_add_to_queue_container").onclick = {
      gridItemClickEvents.offer(ClickEvent.AddSearchResultToQueue(vm.id))
    }

    rootElement.appendChild(container)
  }

  override fun onDestroy() {
    //no-op
  }

  private fun getGridItemCSS(): String =
    """
    .area_content_content_grid_item {
      display: flex;
      flex-direction: column;
      flex: 1;
      padding: 10px;
    }

    .area_content_content_grid_item_image {
      display: flex;
      flex: 1;
      width: -webkit-fill-available;
      width: -moz-available;
      height: auto;
      object-fit: cover;
      background-color: ${Color.BACKGROUND_CONTENT_GRID_IMAGE};
    }

    .area_content_content_grid_item_track_info_container {
      display: flex;
      flex: 0;
      flex-direction: row;
      justify-content: space-between;
      align-items: flex-start;
      margin-top: 10px;
    }

    .area_content_content_grid_item_track_title_and_artist_container {
      display: flex;
      flex: 1;
      flex-direction: column;
      justify-content: flex-start;
    }

    .area_content_content_grid_item_track_title {
      margin: 0;
      font-size: 1.0rem;
    }

    .area_content_content_grid_item_artist_name {
      margin: 0;
      margin-top: 3px;
      font-size: 0.7rem;
    }

    .area_content_content_grid_item_image_container {
      display: inline-flex;
      position: relative;
    }

    .area_content_content_grid_item_image_container img {
      display: block;
    }

    .area_content_content_grid_item_image_container .icon_area_content_content_grid_item_hover_play {
      position: absolute;
      left: 50%;
      margin-left: -20px;
      top: 50%;
      margin-top: -20px;
      font-size: 40px;
      color: ${Color.PRIMARY_GREEN};
    }

    .area_content_content_grid_item_hover_play_container {
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      opacity: 0;
      transition: .3s ease;
    }

    .area_content_content_grid_item_image_container:hover .area_content_content_grid_item_hover_play_container {
      opacity: 1;
    }
    
    .icon_content_content_grid_item_add_to_queue {
      font-size: 20px;
    }
    """.trimIndent()
}
