package ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BasePresenterImpl<View : BaseView> :
  BasePresenter<View>,
  /*
  FIXME:
    Is Dispatchers.Main the right idea here?
  */
  CoroutineScope by CoroutineScope(Dispatchers.Main) {

  protected lateinit var view: View

  abstract fun onStart()
  abstract fun onStop()

  final override fun start(view: View) {
    /*
    Start lifecycle code specific
    to this class will be written
    here BEFORE actual implementation's
    onStart is invoked.
     */
    this.view = view
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
