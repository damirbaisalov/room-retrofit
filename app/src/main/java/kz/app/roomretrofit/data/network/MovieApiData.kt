package kz.app.roomretrofit.data.network

import com.google.gson.annotations.SerializedName
import kz.app.roomretrofit.data.database.Movie

data class MovieApiData(
    val id: Int,
    val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("poster_path") val posterPath: String
)

data class MovieResponse(
    @SerializedName("results") val results: List<MovieApiData>
)

internal fun MovieApiData.toMovieDb(): Movie {
    return Movie(
        id = id,
        overview = overview,
        title = title,
        releaseDate = releaseDate,
        voteAverage = voteAverage.toString(),
        posterPath = posterPath
    )
}