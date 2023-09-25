package data.di

import data.remote.ApiImpl
import data.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ui.AppViewModel
import ui.detail.MovieDetailViewModel
import ui.home.NowPlayingViewModel
import ui.popular.PopularViewModel
import ui.toprated.TopRatedViewModel
import ui.upcoming.UpcomingViewModel

@OptIn(ExperimentalCoroutinesApi::class)
val repoModule = module {
    single { ApiImpl() }

    single { MovieRepository(get()) }

    factory { AppViewModel(get()) }

    factory { MovieDetailViewModel(get()) }
    factory { NowPlayingViewModel(get()) }
    factory { PopularViewModel(get()) }
    factory { TopRatedViewModel(get()) }
    factory { UpcomingViewModel(get()) }

}
