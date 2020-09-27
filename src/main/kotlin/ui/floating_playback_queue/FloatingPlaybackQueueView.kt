package ui.floating_playback_queue

import config.Color
import extensions.createHtmlElementWithId
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLStyleElement
import ui.base.Renderable
import kotlin.browser.document

class FloatingPlaybackQueueView(
  override val rootElement: HTMLElement
) : Renderable(rootElement), FloatingPlaybackQueueContract.View {

  private lateinit var container: HTMLElement

  private val presenter: FloatingPlaybackQueueContract.Presenter = FloatingPlaybackQueuePresenterImpl()

  override fun initLayout() {
    setupCSS()

    container = setupHTML()
    container.hidden = true

    rootElement.appendChild(container)
  }

  private fun setupHTML(): HTMLElement {
    val container = document.createHtmlElementWithId(
      localName = "div",
      id = "area_bottom_play_menu_floating_playback_queue_container",
      applyCSS = null
    )
    container.innerHTML = """
      <div id="area_bottom_play_menu_floating_playback_queue_flex_container">
        <div id="area_bottom_play_menu_floating_playback_queue_header">
          <p id="area_bottom_play_menu_floating_playback_queue_header_queue_label">Queue</p>
          <p id="area_bottom_play_menu_floating_playback_queue_header_clear_label">CLEAR</p>
        </div>
        <div id="area_bottom_play_menu_floating_playback_queue_content_container">
          <div id="area_bottom_play_menu_floating_playback_queue_empty_content_container" hidden>
            <p id="area_bottom_play_menu_floating_playback_queue_empty_content_container_label">No playback items</p>
          </div>
          <div id="area_bottom_play_menu_floating_playback_queue_list_container">
          
            <div class="area_bottom_play_menu_floating_playback_queue_list_item">
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_content_container">
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_play_image_container">
                  <i class="far fa-play-circle icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play"></i>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_container">
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title">Sleep</p>
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist">Godspeed You! Black Emperor</p>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_menu_container">
                  <i class="fa fa-minus-circle icon_bottom_play_menu_floating_playback_queue_list_item_menu"></i>
                </div>
              </div>
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_footer_divider"></div>
            </div>
            
            <div class="area_bottom_play_menu_floating_playback_queue_list_item">
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_content_container">
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_play_image_container">
                  <i class="far fa-play-circle icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play"></i>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_container">
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title">Sleep</p>
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist">Godspeed You! Black Emperor</p>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_menu_container">
                  <i class="fa fa-minus-circle icon_bottom_play_menu_floating_playback_queue_list_item_menu"></i>
                </div>
              </div>
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_footer_divider"></div>
            </div>
            
            <div class="area_bottom_play_menu_floating_playback_queue_list_item">
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_content_container">
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_play_image_container">
                  <i class="far fa-play-circle icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play"></i>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_container">
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title">Sleep</p>
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist">Godspeed You! Black Emperor</p>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_menu_container">
                  <i class="fa fa-minus-circle icon_bottom_play_menu_floating_playback_queue_list_item_menu"></i>
                </div>
              </div>
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_footer_divider"></div>
            </div>
            
            <div class="area_bottom_play_menu_floating_playback_queue_list_item">
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_content_container">
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_play_image_container">
                  <i class="far fa-play-circle icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play"></i>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_container">
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title">Sleep</p>
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist">Godspeed You! Black Emperor</p>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_menu_container">
                  <i class="fa fa-minus-circle icon_bottom_play_menu_floating_playback_queue_list_item_menu"></i>
                </div>
              </div>
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_footer_divider"></div>
            </div>
            
            <div class="area_bottom_play_menu_floating_playback_queue_list_item">
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_content_container">
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_play_image_container">
                  <i class="far fa-play-circle icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play"></i>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_container">
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title">Sleep</p>
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist">Godspeed You! Black Emperor</p>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_menu_container">
                  <i class="fa fa-minus-circle icon_bottom_play_menu_floating_playback_queue_list_item_menu"></i>
                </div>
              </div>
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_footer_divider"></div>
            </div>
            
            <div class="area_bottom_play_menu_floating_playback_queue_list_item">
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_content_container">
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_play_image_container">
                  <i class="far fa-play-circle icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play"></i>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_container">
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title">Sleep</p>
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist">Godspeed You! Black Emperor</p>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_menu_container">
                  <i class="fa fa-minus-circle icon_bottom_play_menu_floating_playback_queue_list_item_menu"></i>
                </div>
              </div>
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_footer_divider"></div>
            </div>
            
            <div class="area_bottom_play_menu_floating_playback_queue_list_item">
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_content_container">
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_play_image_container">
                  <i class="far fa-play-circle icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play"></i>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_container">
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title">Sleep</p>
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist">Godspeed You! Black Emperor</p>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_menu_container">
                  <i class="fa fa-minus-circle icon_bottom_play_menu_floating_playback_queue_list_item_menu"></i>
                </div>
              </div>
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_footer_divider"></div>
            </div>
            
            <div class="area_bottom_play_menu_floating_playback_queue_list_item">
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_content_container">
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_play_image_container">
                  <i class="far fa-play-circle icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play"></i>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_container">
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title">Sleep</p>
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist">Godspeed You! Black Emperor</p>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_menu_container">
                  <i class="fa fa-minus-circle icon_bottom_play_menu_floating_playback_queue_list_item_menu"></i>
                </div>
              </div>
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_footer_divider"></div>
            </div>
            
            <div class="area_bottom_play_menu_floating_playback_queue_list_item">
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_content_container">
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_play_image_container">
                  <i class="far fa-play-circle icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play"></i>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_container">
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title">Sleep</p>
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist">Godspeed You! Black Emperor</p>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_menu_container">
                  <i class="fa fa-minus-circle icon_bottom_play_menu_floating_playback_queue_list_item_menu"></i>
                </div>
              </div>
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_footer_divider"></div>
            </div>
            
            <div class="area_bottom_play_menu_floating_playback_queue_list_item">
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_content_container">
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_play_image_container">
                  <i class="far fa-play-circle icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play"></i>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_container">
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title">Sleep</p>
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist">Godspeed You! Black Emperor</p>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_menu_container">
                  <i class="fa fa-minus-circle icon_bottom_play_menu_floating_playback_queue_list_item_menu"></i>
                </div>
              </div>
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_footer_divider"></div>
            </div>
            
            <div class="area_bottom_play_menu_floating_playback_queue_list_item">
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_content_container">
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_play_image_container">
                  <i class="far fa-play-circle icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play"></i>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_container">
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title">Sleep</p>
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist">Godspeed You! Black Emperor</p>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_menu_container">
                  <i class="fa fa-minus-circle icon_bottom_play_menu_floating_playback_queue_list_item_menu"></i>
                </div>
              </div>
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_footer_divider"></div>
            </div>
            
            <div class="area_bottom_play_menu_floating_playback_queue_list_item">
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_content_container">
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_play_image_container">
                  <i class="far fa-play-circle icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play"></i>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_container">
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title">Sleep</p>
                  <p class="area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist">Godspeed You! Black Emperor</p>
                </div>
                <div class="area_bottom_play_menu_floating_playback_queue_list_item_menu_container">
                  <i class="fa fa-minus-circle icon_bottom_play_menu_floating_playback_queue_list_item_menu"></i>
                </div>
              </div>
              <div class="area_bottom_play_menu_floating_playback_queue_list_item_footer_divider"></div>
            </div>
            
          </div>
        </div>
      </div>
    """.trimIndent()
    return container
  }

  private fun setupCSS() {
    val styleElement = document.createElement("style") as HTMLStyleElement
    styleElement.innerText = """
      #area_bottom_play_menu_floating_playback_queue_container {
        position: absolute;
        width: 50vw;
        height: 50vh;
        bottom: 110%;
        right: 10px;
        z-index: 2;
        box-shadow: 0 0 5px ${Color.BACKGROUND_SHADOW_GRADIENT_LIGHT}, -5px -5px 5px ${Color.BACKGROUND_SHADOW_GRADIENT_DARK};
        background: ${Color.BACKGROUND_BLACK};
      }
      
      #area_bottom_play_menu_floating_playback_queue_flex_container {
        display: flex;
        flex-direction: column;
        height: 100%;
      }

      #area_bottom_play_menu_floating_playback_queue_header {
        display: flex;
        flex: 0;
        flex-direction: row;
        justify-content: space-between;
        align-items: baseline;
        padding-left: 10px;
        padding-right: 10px;
      }

      #area_bottom_play_menu_floating_playback_queue_header_queue_label {
        font-size: 1.2rem;
      }

      #area_bottom_play_menu_floating_playback_queue_header_clear_label {
        font-size: 1rem;
        background: mediumseagreen;
        color: #202020;
        padding: 10px;
        border-radius: 4px;
      }

      #area_bottom_play_menu_floating_playback_queue_content_container {
        display: flex;
        flex: 1;
        flex-direction: column;
        overflow-y: scroll;
        overflow-x: hidden;
      }

      #area_bottom_play_menu_floating_playback_queue_empty_content_container {
        display: none;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        flex: 1;
      }

      #area_bottom_play_menu_floating_playback_queue_empty_content_container_label {
        font-size: 1.2rem;
      }
      
      #area_bottom_play_menu_floating_playback_queue_list_container {
        display:flex;
        flex: 1;
        flex-direction: column;
      }

      .area_bottom_play_menu_floating_playback_queue_list_item {
        display: flex;
        flex-direction: column;
      }
      
      .area_bottom_play_menu_floating_playback_queue_list_item:hover {
        background: ${Color.BACKGROUND_BLACK_HIGHLIGHT_ON_HOVER_PLAYBACK_QUEUE_ITEM}
      }
      
      .area_bottom_play_menu_floating_playback_queue_list_item_content_container {
        display: flex;
        flex-direction: row;
        flex: 0;
        padding-left: 15px;
        padding-right: 15px;
        padding-top: 10px;
        padding-bottom: 10px;
      }
      
      .area_bottom_play_menu_floating_playback_queue_list_item_footer_divider {
        height: 1px;
        width: 100%;
        background: ${Color.BOTTOM_DIVIDER_PLAYBACK_QUEUE_ITEM};
      }
      
      .area_bottom_play_menu_floating_playback_queue_list_item_play_image_container {
        flex: 0;
        display: flex;
        flex-direction: column;
        justify-content: center;
      }

      .area_bottom_play_menu_floating_playback_queue_list_item_text_info_container {
        flex: 1;
        display: flex;
        flex-direction: column;
        margin-left: 10px;
        margin-right: 10px;
      }
      
      .area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_title {
        margin-top: 0px;
        margin-bottom: 0px;
        font-size: 1.2rem;
      }
      
      .area_bottom_play_menu_floating_playback_queue_list_item_text_info_track_artist {
        margin-top: 0px;
        margin-bottom: 0px;
        font-size: 0.8rem;
      }
      
      .area_bottom_play_menu_floating_playback_queue_list_item_menu_container {
        flex: 0;
        display: flex;
        flex-direction: column;
        justify-content: center;
      }
      
      .icon_area_bottom_play_menu_floating_playback_queue_list_item_hover_play {
        font-size: 25px;  
      }
      
      .icon_bottom_play_menu_floating_playback_queue_list_item_menu {
        font-size: 20px;
      }
    """.trimIndent()
    document.head!!.appendChild(styleElement)
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
