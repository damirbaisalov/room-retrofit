package kz.app.roomretrofit.data.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.app.roomretrofit.data.network.MovieApiService
import kz.app.roomretrofit.data.network.ResponseResult
import kz.app.roomretrofit.data.network.toMovieDb
import kz.app.roomretrofit.presentation.models.MovieUiModel
import kz.app.roomretrofit.presentation.models.toDbModel
import kz.app.roomretrofit.presentation.models.toUiModel

class MovieRepository(
    private val movieDao: MovieDao,
    private val apiService: MovieApiService
) {

    suspend fun fetchTopRatedMovies(): ResponseResult<List<MovieUiModel>> {
        return try {
            val cachedMovies = movieDao.getAllMovies()
            if (cachedMovies.isNotEmpty()) {
                val movies = cachedMovies.map { it.toUiModel() }
                return ResponseResult.Success(movies)
            }
            val response = apiService.getTopRatedMovies() //API
            val moviesDb = response.results.map { it.toMovieDb() }
            movieDao.insertAll(moviesDb)
            val moviesUi = response.results.map { it.toUiModel() }
            ResponseResult.Success(moviesUi)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }
    }

    suspend fun addMovie(id: Int, errorCallback: (String) -> Unit) {
        try {
            val movie = apiService.getMovieDetails(movieId = id)
            movieDao.insertMovie(movie.toMovieDb())
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { errorCallback.invoke(e.toString()) }
        }
    }

    suspend fun updateMovieDb(movie: MovieUiModel) {
        movieDao.updateMovie(movie.toDbModel())
    }

    suspend fun deleteMovieDb(movie: MovieUiModel) {
        movieDao.deleteMovie(movie.toDbModel())
    }

    suspend fun deleteAllMoviesDb() {
        movieDao.deleteAllMovies()
    }

    suspend fun getMovieByIdDb(movieId: Int): MovieUiModel? {
        val movie = movieDao.getMovieById(movieId)?.toUiModel()
        return movie
    }

    suspend fun getAllMoviesDb(): List<MovieUiModel> {
        val movies = movieDao.getAllMovies().map { it.toUiModel() }
        return movies
    }
}
