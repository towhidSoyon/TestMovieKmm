package ui.toprated

import data.model.MovieItem
import data.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import util.network.DataState

class TopRatedViewModel (private val repo : MovieRepository): ViewModel() {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    val topRatedMovieResponse = MutableStateFlow<DataState<List<MovieItem>>?>(DataState.Loading)

    fun nowPlayingView(page: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            repo.topRatedMovie(page).collectLatest {
                topRatedMovieResponse.value = it
            }
        }
    }
}