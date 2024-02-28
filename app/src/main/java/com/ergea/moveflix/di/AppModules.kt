package com.ergea.moveflix.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.ergea.moveflix.data.network.api.service.MovieService
import com.ergea.moveflix.data.repository.GenreRepository
import com.ergea.moveflix.data.repository.GenreRepositoryImpl
import com.ergea.moveflix.presentation.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module


object AppModules {
    private val networkModule = module {
        single { ChuckerInterceptor(androidContext()) }
        single { MovieService.invoke(get()) }
    }

    private val repositoryModule = module {
        single<GenreRepository> { GenreRepositoryImpl(get()) }
    }

    private val viewModelModule = module {
        viewModelOf(::HomeViewModel)
    }

    val modules: List<Module> = listOf(
        networkModule,
        repositoryModule,
        viewModelModule
    )
}