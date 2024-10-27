package kz.app.roomretrofit.presentation.models

import kz.app.roomretrofit.data.database.Movie
import kz.app.roomretrofit.data.network.MovieApiData

data class MovieUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val releaseDate: String,
    val rating: String,
    val posterPath: String
)

internal fun MovieApiData.toUiModel(): MovieUiModel {
    return MovieUiModel(
        id = id,
        title = title,
        description = overview,
        releaseDate = releaseDate,
        rating = voteAverage.toString(),
        posterPath = posterPath
    )
}

internal fun Movie.toUiModel(): MovieUiModel {
    return MovieUiModel(
        id = id,
        title = title,
        description = overview,
        releaseDate = releaseDate,
        rating = voteAverage,
        posterPath = posterPath
    )
}

internal fun MovieUiModel.toDbModel(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = description,
        releaseDate = releaseDate,
        voteAverage = rating,
        posterPath = posterPath
    )
}