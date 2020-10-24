package extensions

import org.w3c.dom.Document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.css.CSSStyleDeclaration

fun Document.createHtmlElementWithId(
  localName: String,
  id: String,
  applyCSS: ((style: CSSStyleDeclaration) -> Unit)? = null
): HTMLElement {
  val htmlElement = createElement(localName) as HTMLElement
  htmlElement.id = id
  if(applyCSS != null) applyCSS(htmlElement.style)
  return htmlElement
}

fun Document.createHtmlElementWithClass(
  localName: String,
  clazz: String,
  applyCSS: ((style: CSSStyleDeclaration) -> Unit)? = null
): HTMLElement {
  val htmlElement = createElement(localName) as HTMLElement
  htmlElement.className = clazz
  if (applyCSS != null) applyCSS(htmlElement.style)
  return htmlElement
}

fun Document.createIcon(
  clazz: String,
  id: String,
  applyIconCss: (style: CSSStyleDeclaration) -> Unit,
  applyContainerCss: ((style: CSSStyleDeclaration) -> Unit)? = null
): HTMLElement {
  val icon = createElement("svg") as HTMLElement
  icon.className = clazz
  icon.id = id
  applyIconCss(icon.style)

  val container = createHtmlElementWithId(
    localName = "div",
    id = "${id}_container",
    applyCSS = applyContainerCss
  )
  container.appendChild(icon)
  return container
}

fun Document.createHtmlInputElement(
  type: String,
  placeholder: String,
  id: String,
  applyCSS: (style: CSSStyleDeclaration) -> Unit
): HTMLInputElement {
  val inputElement =
    createHtmlElementWithId(
      localName = "input",
      id = id,
      applyCSS = applyCSS
    ) as HTMLInputElement
  return inputElement.apply {
    this.type = type
    this.placeholder = placeholder
  }
}

fun Document.getHTMLElementById(id: String): HTMLElement =
  this.getElementById(id) as HTMLElement
