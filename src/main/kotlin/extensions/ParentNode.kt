package extensions

import org.w3c.dom.HTMLElement
import org.w3c.dom.ParentNode
import org.w3c.dom.asList

fun ParentNode.querySelectorAsHtmlElement(selectors: String): HTMLElement =
  querySelector(selectors) as HTMLElement

fun ParentNode.querySelectorAllAsHtmlElements(selectors: String): List<HTMLElement> =
  querySelectorAll(selectors).asList()
    .map { node -> node as HTMLElement }
