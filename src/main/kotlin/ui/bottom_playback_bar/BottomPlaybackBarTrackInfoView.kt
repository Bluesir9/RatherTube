package ui.bottom_playback_bar

import extensions.createHtmlElementWithId
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLParagraphElement
import org.w3c.dom.css.CSSStyleDeclaration
import ui.base.Renderable
import kotlin.browser.document

class BottomPlaybackBarTrackInfoView(
  override val rootElement: HTMLElement
) : Renderable(rootElement) {

  private lateinit var songNameElement: HTMLParagraphElement
  private lateinit var artistNameElement: HTMLParagraphElement

  override fun initLayout() {
    songNameElement = document.createHtmlElementWithId(
      localName = "p",
      id = "area_bottom_play_menu_track_information_song_name",
      applyCSS = applySongNameCSS
    ) as HTMLParagraphElement

    artistNameElement = document.createHtmlElementWithId(
      localName = "p",
      id = "area_bottom_play_menu_track_information_artist_name",
      applyCSS = applyArtistNameCSS
    ) as HTMLParagraphElement

    rootElement.appendChild(songNameElement)
    rootElement.appendChild(artistNameElement)
  }

  private val applySongNameCSS: (style: CSSStyleDeclaration) -> Unit = { style ->
    style.fontSize = "1.0rem"
    style.margin = "0"
    style.marginBottom = "3px"
  }

  private val applyArtistNameCSS: (style: CSSStyleDeclaration) -> Unit = { style ->
    style.fontSize = "0.7rem"
    style.margin = "0"
    style.marginTop = "3px"
  }

  override fun onLayoutReady() {
    //no-op
  }

  fun render(vm: BottomPlaybackBarVM) {
    songNameElement.innerHTML = vm.trackTitle
    artistNameElement.innerHTML = vm.trackArtist
  }

  override fun onDestroy() {
    //no-op
  }
}
