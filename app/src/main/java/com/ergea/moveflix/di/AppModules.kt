package com.ergea.moveflix.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.ergea.moveflix.data.network.api.service.MovieService
import com.ergea.moveflix.data.repository.MovieRepository
import com.ergea.moveflix.data.repository.MovieRepositoryImpl
import com.ergea.moveflix.presentation.detail.DetailViewModel
import com.ergea.moveflix.presentation.home.HomeViewModel
import com.ergea.moveflix.presentation.movelist.MovieListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module


object AppModules {
    private val networkModule = module {
        single { ChuckerInterceptor(androidContext()) }
        single { MovieService.invoke(get()) }
    }

    private val repositoryModule = module {
        single<MovieRepository> { MovieRepositoryImpl(get()) }
    }

    private val viewModelModule = module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::MovieListViewModel)
        viewModelOf(::DetailViewModel)
    }

    val modules: List<Module> = listOf(
        networkModule,
        repositoryModule,
        viewModelModule
    )
}