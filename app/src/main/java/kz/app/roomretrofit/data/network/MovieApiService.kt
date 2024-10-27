package kz.app.roomretrofit.data.network

import kz.app.roomretrofit.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Header("Authorization") accessToken: String = BuildConfig.accessToken,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Header("Authorization") accessToken: String = BuildConfig.accessToken,
        @Path("movie_id") movieId: Int
    ): MovieApiData
}