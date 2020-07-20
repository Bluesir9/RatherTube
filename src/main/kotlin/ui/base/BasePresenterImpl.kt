package ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BasePresenterImpl :
  BasePresenter,
  /*
  FIXME:
    Is Dispatchers.Main the right idea here?
  */
  CoroutineScope by CoroutineScope(Dispatchers.Main) {

  abstract fun onStart()
  abstract fun onStop()

  final override fun start() {
    /*
    Start lifecycle code specific
    to this class will be written
    here BEFORE actual implementation's
    onStart is invoked.
     */
    onStart()
  }

  final override fun stop() {
    onStop()
    cancel()
    /*
    Stop lifecycle code specific
    to this class will be written
    here AFTER actual implementation's
    onStop callback has been invoked.
     */
  }
}
