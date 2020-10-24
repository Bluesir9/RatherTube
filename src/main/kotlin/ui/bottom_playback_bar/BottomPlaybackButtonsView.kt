package ui.bottom_playback_bar

import config.Color
import extensions.clickEventsAsFlow
import extensions.createIcon
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.HTMLElement
import ui.base.Renderable
import kotlin.browser.document

@Suppress("EXPERIMENTAL_API_USAGE")
class BottomPlaybackButtonsView(
  override val rootElement: HTMLElement
) : Renderable(rootElement) {

  private lateinit var previousTrackButton: HTMLElement
  private lateinit var pauseTrackButton: HTMLElement
  private lateinit var playTrackButton: HTMLElement
  private lateinit var nextTrackButton: HTMLElement

  override fun initLayout() {
    previousTrackButton = makePreviousButton().also { rootElement.appendChild(it) }
    pauseTrackButton =
      makePauseButton().also {
        /*
        Visibility of play and pause button is mutually
        exclusive. So for initial render, pause track
        will be hidden and play track will be shown.
         */
        it.hidden = true
        rootElement.appendChild(it)
      }
    playTrackButton = makePlayButton().also { rootElement.appendChild(it) }
    nextTrackButton = makeNextButton().also { rootElement.appendChild(it) }
  }

  private fun makePreviousButton(): HTMLElement =
    document.createIcon(
      clazz = "fas fa-step-backward",
      id = "icon_bottom_play_menu_previous_track_button",
      applyIconCss = { style ->
        style.fontSize = "20px"
        style.color = Color.PRIMARY_GREEN
      },
      applyContainerCss = { style ->
        style.padding = "10px"
      }
    )

  private fun makePauseButton(): HTMLElement =
    document.createIcon(
      clazz = "far fa-pause-circle",
      id = "icon_bottom_play_menu_pause_track_button",
      applyIconCss = { style ->
        style.fontSize = "40px"
        style.color = Color.PRIMARY_GREEN
      },
      applyContainerCss = { style ->
        style.padding = "10px"
      }
    )

  private fun makePlayButton(): HTMLElement =
    document.createIcon(
      clazz = "far fa-play-circle",
      id = "icon_bottom_play_menu_play_track_button",
      applyIconCss = { style ->
        style.fontSize = "40px"
        style.color = Color.PRIMARY_GREEN
      },
      applyContainerCss = { style ->
        style.padding = "10px"
      }
    )

  private fun makeNextButton(): HTMLElement =
    document.createIcon(
      clazz = "fas fa-step-forward",
      id = "icon_bottom_play_menu_next_track_button",
      applyIconCss = { style ->
        style.fontSize = "20px"
        style.color = Color.PRIMARY_GREEN
      },
      applyContainerCss = { style ->
        style.padding = "10px"
      }
    )

  override fun onLayoutReady() {
    //no-op
  }

  fun render(vm: BottomPlaybackBarVM) {
    playTrackButton.hidden = !vm.playButtonVisible
    pauseTrackButton.hidden = !vm.pauseButtonVisible
  }

  fun getPreviousButtonClickEvents(): Flow<Unit> = previousTrackButton.clickEventsAsFlow()
  fun getPauseButtonClickEvents(): Flow<Unit> = pauseTrackButton.clickEventsAsFlow()
  fun getPlayButtonClickEvents(): Flow<Unit> = playTrackButton.clickEventsAsFlow()
  fun getNextButtonClickEvents(): Flow<Unit> = nextTrackButton.clickEventsAsFlow()

  override fun onDestroy() {
    //no-op
  }
}
