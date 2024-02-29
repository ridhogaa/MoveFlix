package com.ergea.moveflix.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergea.moveflix.data.repository.MovieRepository
import com.ergea.moveflix.model.Movie
import com.ergea.moveflix.model.MovieVideo
import com.ergea.moveflix.model.Review
import com.ergea.moveflix.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch


class DetailViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _movieResponse =
        MutableStateFlow<Resource<Movie>>(Resource.Loading())
    val movieResponse = _movieResponse.asStateFlow()

    private val _reviewResponse =
        MutableStateFlow<Resource<List<Review>>>(Resource.Loading())
    val reviewResponse = _reviewResponse.asStateFlow()

    private val _movieVideoResponse =
        MutableStateFlow<Resource<List<MovieVideo>>>(Resource.Loading())
    val movieVideoResponse = _movieVideoResponse.asStateFlow()

    fun getMovieById(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.getMovieById(id).collect {
            _movieResponse.value = it
        }
    }

    fun getReviewsByMovieId(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.getReviewsByMovieId(id)
            .collectLatest {
                _reviewResponse.value = it
            }
    }

    fun getMovieVideo(movieId: Int) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.getMovieVideo(movieId).collectLatest {
            _movieVideoResponse.value = it
        }
    }
}