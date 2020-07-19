package ui.top_search_bar

import ui.base.BasePresenter
import ui.base.BaseView

interface TopSearchBarContract {
  interface View : BaseView {
    fun render(vm : TopSearchBarVM)
  }

  interface Presenter : BasePresenter {
    fun onSearchQueryDecided(query: String)
  }
}
