@file:Suppress("EXPERIMENTAL_API_USAGE")

package ui.bottom_playback_bar

import config.Color
import extensions.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import logging.Logger
import logging.LoggerImpl
import org.w3c.dom.HTMLElement
import ui.base.Renderable
import ui.bottom_playback_bar.BottomPlaybackBarVM.ProgressBarVM.Hidden
import ui.bottom_playback_bar.BottomPlaybackBarVM.ProgressBarVM.Visible
import kotlin.browser.document

class BottomPlaybackBarProgressView(
  override val rootElement: HTMLElement
) : Renderable(rootElement) {

  companion object {
    private const val PROGRESS_DOT_WIDTH_IN_PIXELS = 15
  }

  data class ProgressBarClickEvent(
    val barWidth: Int,
    val clickPositionFromLeft: Int
  )

  private val logger: Logger = LoggerImpl(BottomPlaybackBarProgressView::class.simpleName!!)

  private lateinit var progressBar: HTMLElement
  private lateinit var progressDot: HTMLElement
  private lateinit var progressTimestamp: HTMLElement

  private val progressBarClickEvents = BroadcastChannel<ProgressBarClickEvent>(1)

  private val progressBarContainerWidth get() = rootElement.offsetWidth

  override fun initLayout() {
    progressBar =
      document.createHtmlElementWithId(
        localName = "div",
        id = "area_bottom_progress_bar_container_progress",
        applyCSS = { style ->
          style.background = Color.PROGRESS_BAR_PROGRESS
          style.height = "5px"
        }
      )

    progressDot =
      document.createHtmlElementWithId(
        localName = "div",
        id = "area_bottom_progress_bar_container_dot",
        applyCSS = { style ->
          style.position = "absolute"
          style.background = Color.PROGRESS_BAR_DOT
          style.width = "${PROGRESS_DOT_WIDTH_IN_PIXELS}px"
          style.height = "15px"
          style.borderRadius = "50%"
        }
      )

    progressTimestamp =
      document.createHtmlElementWithId(
        localName = "div",
        id = "area_bottom_progress_bar_container_timestamp",
        applyCSS = { style ->
          style.position = "absolute"
          style.right = "10px"
          style.top = "10px"
          style.fontSize = "0.9rem"
        }
      )

    rootElement.append(progressBar, progressDot, progressTimestamp)

    rootElement.onclick = { mouseEvent ->
      progressBarClickEvents.offer(ProgressBarClickEvent(progressBarContainerWidth, mouseEvent.clientX))
    }

    rootElement.hide()
  }

  fun render(vm: BottomPlaybackBarVM) {

    when(vm.progressBarVM) {
      is Hidden -> renderHiddenProgressBar()
      is Visible -> renderVisibleProgressBar(vm.progressBarVM)
    }
  }

  private fun renderHiddenProgressBar() {
    rootElement.hide()
  }

  private fun renderVisibleProgressBar(vm: Visible) {
    rootElement.show(StyleDisplay.Flex)
    renderProgressBarWidth(vm.progressPercentage)
    renderProgressDot(vm.progressPercentage)
    progressTimestamp.innerText = vm.progressTimestamp
  }

  private fun renderProgressBarWidth(progressPercentage: Double) {
    progressBar.style.width = "${progressPercentage}%"
  }

  /*
  We want to move a circular dot that is 15 px in diameter from left to right.
  We will do this by increasing its margin until it reaches the right edge of the screen.
  The value of this margin will be calculated by using the percentage of progress we want to
  indicate for the track playing.

  So say we have a screen that is a 1000 pixels wide and we wish to indicate that 50% progress
  has been made, the value of `marginLeftInPixels` will be calculated as follows:

  (1000 - 0) * 50 / 100 = 500

  Here, the calculated 500 is supposed to mean pixels.
  */
  private fun renderProgressDot(progressPercentage: Double) {
    val progressBarContainerWidthInPixels = progressBarContainerWidth
    val dotWidthInPixels = PROGRESS_DOT_WIDTH_IN_PIXELS
    val minMarginLeftInPixels = 0
    val maxMarginLeftInPixels = progressBarContainerWidthInPixels - dotWidthInPixels
    val marginLeftInPixels = (maxMarginLeftInPixels - minMarginLeftInPixels) * progressPercentage / 100
    progressDot.style.marginLeft = "${marginLeftInPixels}px"
  }

  fun getProgressBarClickEvents(): Flow<ProgressBarClickEvent> = progressBarClickEvents.asFlow()

  override fun onLayoutReady() {
    //no-op
  }

  override fun onDestroy() {
    //no-op
  }
}