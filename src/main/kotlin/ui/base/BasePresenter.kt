package ui.base

interface BasePresenter<View : BaseView> {
  fun start(view: View)
  fun stop()
}
