package ui.bottom_playback_bar

import config.Color
import extensions.StyleDisplay
import extensions.createHtmlElementWithId
import extensions.hide
import extensions.show
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

  private val logger: Logger = LoggerImpl(BottomPlaybackBarProgressView::class.simpleName!!)

  private lateinit var progressBar: HTMLElement
  private lateinit var progressDot: HTMLElement
  private lateinit var progressTimestamp: HTMLElement

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
          style.width = "15px"
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

    rootElement.onmouseover = { event ->
      logger.info("mouse over -> X coordinate = ${event.clientX}")
    }

    rootElement.onmousemove = { event ->
      logger.info("mouse move -> X coordinate = ${event.clientX}")
    }

    rootElement.onmouseout = { event ->
      logger.info("mouse out -> X coordinate = ${event.clientX}")
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
    progressBar.style.width = "${vm.progressBarWidthPercent}%"
    progressDot.style.marginLeft = "${vm.progressDotLeftMarginPercent}%"
    progressTimestamp.innerText = vm.progressTimestamp
  }

  override fun onLayoutReady() {
    //no-op
  }

  override fun onDestroy() {
    //no-op
  }
}