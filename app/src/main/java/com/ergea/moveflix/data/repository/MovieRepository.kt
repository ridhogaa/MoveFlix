package com.ergea.moveflix.data.repository

import com.ergea.moveflix.data.network.api.model.toMovie
import com.ergea.moveflix.data.network.api.service.MovieService
import com.ergea.moveflix.model.Movie
import com.ergea.moveflix.utils.Resource
import com.ergea.moveflix.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query


interface MovieRepository {
    fun getMovieByGenre(genre: String): Flow<Resource<List<Movie>>>
}

class MovieRepositoryImpl(private val service: MovieService) : MovieRepository {
    override fun getMovieByGenre(genre: String): Flow<Resource<List<Movie>>> =
        proceedFlow {
            service.getMovieByGenre(genre).results.map { it.toMovie() }
        }

}