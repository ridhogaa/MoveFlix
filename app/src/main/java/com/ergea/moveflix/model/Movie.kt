package com.ergea.moveflix.model


data class Movie(
    val img: String,
    val name: String,
    val id: Int,
    val overview: String? = null,
    val voteAverage: Double? = null,
    val voteCount: Double? = null,
    val runtime: Int? = null,
    val releaseDate: String? = null,
)