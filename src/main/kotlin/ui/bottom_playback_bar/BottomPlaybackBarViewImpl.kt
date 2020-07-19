package ui.bottom_playback_bar

import extensions.createHtmlElementWithId
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.css.CSSStyleDeclaration
import ui.base.Renderable
import kotlin.browser.document

@Suppress("EXPERIMENTAL_API_USAGE")
class BottomPlaybackBarViewImpl(
  override val rootElement: HTMLElement
) : Renderable(rootElement), BottomPlaybackBarContract.View {

  private lateinit var trackInfoView: BottomPlaybackBarTrackInfoView
  private lateinit var playbackButtonsView: BottomPlaybackButtonsView
  private lateinit var queueButtonView: BottomQueueButtonView

  private val presenter : BottomPlaybackBarContract.Presenter = BottomPlaybackBarPresenterImpl()

  override fun initLayout() {
    val trackInfoContainer =
      document.createHtmlElementWithId(
        localName = "div",
        id = "area_bottom_play_menu_track_information",
        applyCSS = applyTrackInfoContainerCSS
      ).also { document.appendChild(it) }

    val trackPlaybackButtonsContainer =
      document.createHtmlElementWithId(
        localName = "div",
        id = "area_bottom_play_menu_playback_buttons",
        applyCSS = applyTrackPlaybackButtonsContainerCSS
      ).also { document.appendChild(it) }

    val trackQueueButtonContainer =
      document.createHtmlElementWithId(
        localName = "div",
        id = "area_bottom_play_menu_track_queue_button_container",
        applyCSS = applyTrackQueueButtonContainerCSS
      ).also { document.appendChild(it) }

    trackInfoView = BottomPlaybackBarTrackInfoView(trackInfoContainer).also { it.create() }
    playbackButtonsView = BottomPlaybackButtonsView(trackPlaybackButtonsContainer).also { it.create() }
    queueButtonView = BottomQueueButtonView(trackQueueButtonContainer).also { it.create() }

    playbackButtonsView
      .getPreviousButtonClickEvents()
      .onEach { presenter.goToPreviousTrackButtonClick() }
      .launchIn(this)

    playbackButtonsView
      .getPauseButtonClickEvents()
      .onEach { presenter.pauseCurrentTrackButtonClick() }
      .launchIn(this)

    playbackButtonsView
      .getPlayButtonClickEvents()
      .onEach { presenter.playCurrentTrackButtonClick() }
      .launchIn(this)

    playbackButtonsView
      .getNextButtonClickEvents()
      .onEach { presenter.goToNextTrackButtonClick() }
      .launchIn(this)
  }

  private val applyTrackInfoContainerCSS: (style: CSSStyleDeclaration) -> Unit = { style ->
    style.display = "flex"
    style.flex = "1"
    style.flexDirection = "column"
  }

  private val applyTrackPlaybackButtonsContainerCSS: (style: CSSStyleDeclaration) -> Unit = { style ->
    style.display = "flex"
    style.flex = "1"
    style.flexDirection = "row"
    style.justifyContent = "center"
    style.alignItems = "center"
  }

  private val applyTrackQueueButtonContainerCSS: (style: CSSStyleDeclaration) -> Unit = { style ->
    style.display = "flex"
    style.flex = "1"
    style.flexDirection = "row"
    style.justifyContent = "end"
    style.padding = "10px"
  }

  override fun onLayoutReady() {
    presenter.start()
  }

  override fun render(vm: BottomPlaybackBarVM) {
    trackInfoView.render(vm)
    playbackButtonsView.render(vm)
  }

  override fun onDestroy() {
    presenter.stop()
    trackInfoView.destroy()
    playbackButtonsView.destroy()
    queueButtonView.destroy()
  }
}
