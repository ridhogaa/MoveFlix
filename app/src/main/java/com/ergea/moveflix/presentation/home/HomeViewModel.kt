package com.ergea.moveflix.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergea.moveflix.data.repository.MovieRepository
import com.ergea.moveflix.model.Genre
import com.ergea.moveflix.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _genreResponse =
        MutableStateFlow<Resource<List<Genre>>>(Resource.Loading())
    val genreResponse = _genreResponse.asStateFlow()

    fun getGenre() = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.getGenre().collect {
            _genreResponse.value = it
        }
    }
}