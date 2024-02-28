package com.ergea.moveflix.presentation.movelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergea.moveflix.data.repository.MovieRepository
import com.ergea.moveflix.model.Movie
import com.ergea.moveflix.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _movieResponse =
        MutableStateFlow<Resource<List<Movie>>>(Resource.Loading())
    val movieResponse = _movieResponse.asStateFlow()

    fun getMovieByGenre(genre: String) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.getMovieByGenre(genre).collect {
            _movieResponse.value = it
        }
    }
}