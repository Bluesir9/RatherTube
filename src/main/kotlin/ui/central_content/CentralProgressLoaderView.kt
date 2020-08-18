package ui.central_content

import config.Color
import extensions.createHtmlElementWithClass
import extensions.querySelectorAllAsHtmlElements
import extensions.querySelectorAsHtmlElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLStyleElement
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

    val styleElement = document.createElement("style") as HTMLStyleElement
    styleElement.innerText = """
      .area_content_central_loader > div {
        background-color: ${Color.PRIMARY_BLUE};
        height: 100%;
        width: 8px;
        display: inline-block;
        margin: 1px;

        -webkit-animation: sk-stretchdelay 1.2s infinite ease-in-out;
        animation: sk-stretchdelay 1.2s infinite ease-in-out
      }

      .area_content_central_loader .rect2 {
        -webkit-animation-delay: -1.1s;
        animation-delay: -1.1s;
      }

      .area_content_central_loader .rect3 {
        -webkit-animation-delay: -1.0s;
        animation-delay: -1.0s;
      }

      .area_content_central_loader .rect4 {
        -webkit-animation-delay: -0.9s;
        animation-delay: -0.9s;
      }

      .area_content_central_loader .rect5 {
        -webkit-animation-delay: -0.8s;
        animation-delay: -0.8s;
      }

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
    """.trimIndent()
    document.head!!.appendChild(styleElement)
  }

  override fun onLayoutReady() {
    //no-op
  }

  override fun onDestroy() {
    //no-op
  }
}
