package ui.central_content

import extensions.createHtmlElementWithClass
import extensions.querySelectorAsHtmlElement
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLParagraphElement
import org.w3c.dom.asList
import org.w3c.dom.css.CSSStyleSheet
import ui.base.Renderable
import ui.central_content.CentralContentVM.SearchResults
import ui.central_content.CentralContentVM.SearchResults.Item
import uuid.UUID
import kotlin.browser.document
import kotlin.dom.clear

@Suppress("EXPERIMENTAL_API_USAGE")
class CentralSearchResultsView(
  override val rootElement: HTMLElement
) : Renderable(rootElement) {

  private val gridItemClickEvents = BroadcastChannel<UUID>(1)

  override fun initLayout() {
    /*
    FIXME:
      Not sure if this works as intended, but to insert
      the CSS for every grid item present in the content
      grid is definitely quite wrong.
     */
    val css = document.styleSheets.asList().first() as CSSStyleSheet
    css.insertRule(getGridItemCSS(),css.cssRules.length)
  }

  override fun onLayoutReady() {
    //no-op
  }

  fun render(vm: SearchResults) {
    rootElement.clear() //FIXME: How expensive is this?
    vm.items
      .map { this.getGridItem(it) }
      .forEach { rootElement.appendChild(it) }
  }

  fun getGridItemClickEvents(): Flow<UUID> = gridItemClickEvents.asFlow()

  private fun getGridItem(vm: Item): HTMLElement {
    val container = document.createHtmlElementWithClass(
      localName = "div",
      clazz = "area_content_content_grid_item",
      applyCSS = null
    )

    container.innerHTML = """
        <div class="area_content_content_grid_item_image_container">
          <img class="area_content_content_grid_item_image" src="https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTgJiwgqiIcU_lvccboQXHnspbNAYCJmbXpFozoKZsrWF7X5YN9&usqp=CAU"/>
          <div class="area_content_content_grid_item_hover_play_container">
            <i class="far fa-play-circle icon_area_content_content_grid_item_hover_play"></i>
          </div>
        </div>
        <div class="area_content_content_grid_item_track_info_container">
          <div class="area_content_content_grid_item_track_title_and_artist_container">
            <p class="area_content_content_grid_item_track_title">Static</p>
            <p class="area_content_content_grid_item_artist_name">Godspeed You! Black Emperor</p>
          </div>
          <i class="fas fa-ellipsis-v"></i>
        </div>
    """.trimIndent()

    val image = container.querySelectorAsHtmlElement("area_content_content_grid_item_image") as HTMLImageElement
    val trackTitle = container.querySelectorAsHtmlElement("area_content_content_grid_item_track_title") as HTMLParagraphElement
    val trackArtist = container.querySelectorAsHtmlElement("area_content_content_grid_item_track_title") as HTMLParagraphElement

    image.src = vm.trackImageUrl
    trackTitle.innerHTML = vm.trackTitle
    trackArtist.innerHTML = vm.trackArtist

    container.onclick = {
      gridItemClickEvents.offer(vm.id)
    }

    return container
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
      width: auto;
      height: auto;
      object-fit: cover;
      background-color: #2F2F2F;
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
      color: mediumseagreen;
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
    """.trimIndent()
}
