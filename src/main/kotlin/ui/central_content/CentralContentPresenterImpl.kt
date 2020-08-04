package ui.central_content

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import logging.Logger
import logging.LoggerImpl
import song.SongPlaybackCoordinator
import song.SongPlayer
import ui.base.BasePresenterImpl
import ui.central_content.SearchEvent.Loaded
import ui.central_content.SearchEvent.Loading

@ExperimentalCoroutinesApi
class CentralContentPresenterImpl : CentralContentContract.Presenter, BasePresenterImpl<CentralContentContract.View>() {

  private val logger: Logger = LoggerImpl(CentralContentPresenterImpl::class.simpleName!!)
  private val searchEventsListener: SearchEventsListener = SearchResultsUICoordinator
  private val generateVM: CentralContentVMGenerator = CentralContentVMGeneratorImpl()
  private val songPlayer: SongPlayer = SongPlaybackCoordinator

  private lateinit var lastSearchEvent: SearchEvent
  private lateinit var lastVM: CentralContentVM

  override fun onStart() {
    searchEventsListener
      .getSearchEvents()
      .onEach { lastSearchEvent = it }
      .map { generateVM(it) }
      .onEach { lastVM = it }
      .onEach { view.render(it) }
      .launchIn(this)
  }

  override fun onSearchResultClick(id: String) {
    when(val searchEvent = lastSearchEvent) {
      is Loading -> logger.error("Unexpected encounter of search result click event. lastVM = $lastVM")
      is Loaded -> {
        val clickedSong = searchEvent.songs.firstOrNull { song -> song.id == id }
        if (clickedSong != null) songPlayer.playRequested(clickedSong)
        else logger.error("Unable to find clicked song. lastVM = $lastVM, searchEvent = $searchEvent")
      }
    }
  }

  override fun onStop() {
    //no-op
  }
}
