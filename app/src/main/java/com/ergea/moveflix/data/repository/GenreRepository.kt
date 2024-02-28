package com.ergea.moveflix.data.repository

import com.ergea.moveflix.data.network.api.model.toGenre
import com.ergea.moveflix.data.network.api.service.MovieService
import com.ergea.moveflix.model.Genre
import com.ergea.moveflix.utils.Resource
import com.ergea.moveflix.utils.proceedFlow
import kotlinx.coroutines.flow.Flow


interface GenreRepository {
    fun getGenre(): Flow<Resource<List<Genre>>>
}

class GenreRepositoryImpl(private val service: MovieService) : GenreRepository {
    override fun getGenre(): Flow<Resource<List<Genre>>> =
        proceedFlow {
            service.getGenre().genres.map { it.toGenre() }
        }

}