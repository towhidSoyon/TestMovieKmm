package ui.popular

import data.model.MovieItem
import data.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import util.network.DataState

class PopularViewModel(private val repo : MovieRepository):ViewModel() {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    val popularMovieResponse = MutableStateFlow<DataState<List<MovieItem>>?>(DataState.Loading)

    fun nowPlayingView(page: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            repo.popularMovie(page).collectLatest {
                popularMovieResponse.value = it
            }
        }
    }
}