package com.ergea.moveflix.data.repository

import com.ergea.moveflix.data.network.api.model.GetReviewResponse
import com.ergea.moveflix.data.network.api.model.toGenre
import com.ergea.moveflix.data.network.api.model.toMovie
import com.ergea.moveflix.data.network.api.model.toMovieVideo
import com.ergea.moveflix.data.network.api.model.toResult
import com.ergea.moveflix.data.network.api.service.MovieService
import com.ergea.moveflix.model.Genre
import com.ergea.moveflix.model.Movie
import com.ergea.moveflix.model.MovieVideo
import com.ergea.moveflix.model.Review
import com.ergea.moveflix.utils.Resource
import com.ergea.moveflix.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieRepository {
    fun getGenre(): Flow<Resource<List<Genre>>>
    fun getMovieByGenre(genre: String): Flow<Resource<List<Movie>>>
    fun getMovieById(id: Int): Flow<Resource<Movie>>
    fun getReviewsByMovieId(id: Int): Flow<Resource<List<Review>>>
    fun getMovieVideo(movieId: Int): Flow<Resource<List<MovieVideo>>>
}

class MovieRepositoryImpl(private val service: MovieService) : MovieRepository {
    override fun getGenre(): Flow<Resource<List<Genre>>> =
        proceedFlow {
            service.getGenre().genres.map { it.toGenre() }
        }

    override fun getMovieByGenre(genre: String): Flow<Resource<List<Movie>>> =
        proceedFlow {
            service.getMovieByGenre(genre).results.map { it.toMovie() }
        }

    override fun getMovieById(id: Int): Flow<Resource<Movie>> =
        proceedFlow {
            service.getMovieById(id).toMovie()
        }

    override fun getReviewsByMovieId(id: Int): Flow<Resource<List<Review>>> =
        proceedFlow {
            service.getReviewsByMovieId(id).results?.map { it.toResult() } ?: emptyList()
        }

    override fun getMovieVideo(movieId: Int): Flow<Resource<List<MovieVideo>>> =
        proceedFlow {
            service.getMovieVideo(movieId).results.map { it.toMovieVideo() }
        }
}