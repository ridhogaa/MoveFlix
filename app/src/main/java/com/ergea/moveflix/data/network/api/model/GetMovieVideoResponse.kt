package com.ergea.moveflix.data.network.api.model


import com.ergea.moveflix.model.MovieVideo
import com.google.gson.annotations.SerializedName

data class GetMovieVideoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<Result>
) {
    data class Result(
        @SerializedName("iso_639_1")
        val iso6391: String,
        @SerializedName("iso_3166_1")
        val iso31661: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("key")
        val key: String,
        @SerializedName("site")
        val site: String,
        @SerializedName("size")
        val size: Int,
        @SerializedName("type")
        val type: String,
        @SerializedName("official")
        val official: Boolean,
        @SerializedName("published_at")
        val publishedAt: String,
        @SerializedName("id")
        val id: String
    )
}

fun GetMovieVideoResponse.Result.toMovieVideo() = MovieVideo(
    this.iso6391,
    this.iso31661,
    this.name,
    this.key,
    this.site,
    this.size,
    this.type,
    this.official,
    this.publishedAt,
    this.id,
)