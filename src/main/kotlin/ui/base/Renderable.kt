package ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.HTMLElement

/*
Don't feel great about the name but
honestly, I am at a loss of words.

rootElement -> This is the html element inside which
all the child html elements will be added.
 */
abstract class Renderable(
  protected open val rootElement: HTMLElement
): CoroutineScope by CoroutineScope(Dispatchers.Main) {
  /*
  Initial HTML hierarchy will be laid out
  in this APIs implementation.
   */
  protected abstract fun initLayout()

  /*
  Initial HTML hierarchy is ready so we can
  start setting up our presenter and UI hooks
  to add interactivity logic
   */
  protected abstract fun onLayoutReady()

  /*
  Any and all cleanup will be performed in
  this callback. For example the coroutine
  scope will cancel the scope after this is
  called.

  FIXME: Should we cleanup the HTML hierarchy
    as well in this callback?
   */
  protected abstract fun onDestroy()

  fun create() {
    initLayout()
    onLayoutReady()
  }

  fun destroy() {
    onDestroy()
    /*
    Destruction code specific to this
    class will follow from here.
     */
  }
}
