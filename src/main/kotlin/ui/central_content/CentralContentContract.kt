package ui.central_content

import ui.base.BasePresenter
import ui.base.BaseView
import uuid.UUID

interface CentralContentContract {
  interface View : BaseView {
    fun render(vm: CentralContentVM)
  }

  interface Presenter : BasePresenter {
    fun onSearchResultClick(id: UUID)
  }
}
