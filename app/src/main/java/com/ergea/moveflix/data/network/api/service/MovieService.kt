package com.ergea.moveflix.data.network.api.service

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.ergea.moveflix.BuildConfig
import com.ergea.moveflix.data.network.api.model.GetGenreResponse
import com.ergea.moveflix.data.network.api.model.GetMovieResponse
import com.ergea.moveflix.data.network.api.model.GetMovieVideoResponse
import com.ergea.moveflix.data.network.api.model.GetReviewResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface MovieService {

    @GET("genre/movie/list")
    suspend fun getGenre(): GetGenreResponse

    @GET("discover/movie")
    suspend fun getMovieByGenre(
        @Query("with_genre") genre: String
    ): GetMovieResponse

    @GET("movie/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int
    ): GetMovieResponse.Result

    @GET("movie/{id}/reviews")
    suspend fun getReviewsByMovieId(
        @Path("id") id: Int
    ): GetReviewResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideo(
        @Path("movie_id") movieId: Int
    ): GetMovieVideoResponse

    companion object {
        @JvmStatic
        operator fun invoke(chucker: ChuckerInterceptor): MovieService {
            val httpLoggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val okHttpClient = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor {
                    val originalRequest = it.request()
                    val originalHttpUrl = originalRequest.url

                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.API_KEY)
                        .build()

                    val requestBuilder = originalRequest.newBuilder().url(url)
                    val request = requestBuilder.build()

                    return@addInterceptor it.proceed(request)
                }
                .addInterceptor {
                    val request = it.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build()
                    return@addInterceptor it.proceed(request)
                }
                .pingInterval(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(MovieService::class.java)
        }
    }
}