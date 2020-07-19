package ui.base

abstract class BasePresenterImpl : BasePresenter {
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
    /*
    Stop lifecycle code specific
    to this class will be written
    here AFTER actual implementation's
    onStop callback has been invoked.
     */
  }
}
