package ui.central_content

import extensions.createHtmlElementWithClass
import extensions.querySelectorAllAsHtmlElements
import extensions.querySelectorAsHtmlElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList
import org.w3c.dom.css.CSSStyleSheet
import ui.base.Renderable
import kotlin.browser.document

class CentralProgressLoaderView(
  override val rootElement: HTMLElement
) : Renderable(rootElement) {

  override fun initLayout() {
    /*
    We want 5 div elements with names
    rect1, rect2, rect3, rect4 and rect5.
    */
    (1..5)
      .map { number ->
        document.createHtmlElementWithClass(
          "div",
          clazz = "rect$number",
          applyCSS = null
        )
      }.forEach { element -> rootElement.appendChild(element) }

    document.querySelectorAllAsHtmlElements(".area_content_central_loader > div")
      .map { it.style }
      .forEach { style ->
        style.backgroundColor = "mediumseagreen"
        style.height = "100%"
        style.width = "6px"
        style.display = "inline-block"

        style.setPropertyValue("-webkit-animation", "sk-stretchdelay 1.2s infinite ease-in-out")
        style.animation = "sk-stretchdelay 1.2s infinite ease-in-out"
      }

    document.querySelectorAsHtmlElement(".area_content_central_loader .rect2").style
      .apply {
        setPropertyValue("-webkit-animation-delay", "-1.1s")
        animationDelay = "-1.1s"
      }

    document.querySelectorAsHtmlElement(".area_content_central_loader .rect3").style
      .apply {
        setPropertyValue("-webkit-animation-delay", "-1.0s")
        animationDelay = "-1.0s"
      }

    document.querySelectorAsHtmlElement(".area_content_central_loader .rect2").style
      .apply {
        setPropertyValue("-webkit-animation-delay", "-0.9s")
        animationDelay = "-0.9s"
      }

    document.querySelectorAsHtmlElement(".area_content_central_loader .rect2").style
      .apply {
        setPropertyValue("-webkit-animation-delay", "-0.8s")
        animationDelay = "-0.8s"
      }

    /*
    FIXME:
     I am not sure if below approach will work
     but from my research, this is the only
     mechanism via which we can insert animation
     and keyframe related rules to CSS. 
     */
    val css = document.styleSheets.asList().first() as CSSStyleSheet
    css.insertRule("""
      @-webkit-keyframes sk-stretchdelay {
        0%, 40%, 100% { -webkit-transform: scaleY(0.4) }  
        20% { -webkit-transform: scaleY(1.0) }
      }
      
      @keyframes sk-stretchdelay {
        0%, 40%, 100% { 
          transform: scaleY(0.4);
          -webkit-transform: scaleY(0.4);
        }  20% { 
          transform: scaleY(1.0);
          -webkit-transform: scaleY(1.0);
        }
      }
    """.trimIndent(), css.cssRules.length)
  }

  override fun onLayoutReady() {
    //no-op
  }

  override fun onDestroy() {
    //no-op
  }
}
