package com.ergea.moveflix.data.network.api.model


import com.ergea.moveflix.model.Genre
import com.google.gson.annotations.SerializedName

data class GetGenreResponse(
    @SerializedName("genres")
    val genres: List<Genre>
) {
    data class Genre(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
}

fun GetGenreResponse.Genre.toGenre() = Genre(
    id = this.id,
    name = this.name
)

