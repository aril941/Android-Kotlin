package com.example.project.filmapp.module

import com.example.project.core.domain.usecase.FilmAppInteractor
import com.example.project.core.domain.usecase.FilmAppUseCase
import com.example.project.filmapp.detail.DetailViewModel
import com.example.project.filmapp.film.FilmViewModel
import com.example.project.filmapp.home.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val useCaseModule = module {
    factory<FilmAppUseCase> { FilmAppInteractor(get()) }
}

@ExperimentalCoroutinesApi
@FlowPreview
val viewModelModule = module {
    viewModel { FilmViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}