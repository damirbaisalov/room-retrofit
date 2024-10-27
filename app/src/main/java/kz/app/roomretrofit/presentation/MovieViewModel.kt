package kz.app.roomretrofit.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.app.roomretrofit.data.database.MovieRepository
import kz.app.roomretrofit.data.network.ResponseResult
import kz.app.roomretrofit.presentation.models.MovieUiModel

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _topRatedMovies = MutableLiveData<ResponseResult<List<MovieUiModel>>>()
    val topRatedMovies: LiveData<ResponseResult<List<MovieUiModel>>> = _topRatedMovies

    private val _selectedMovie = MutableLiveData<MovieUiModel>()
    val selectedMovie: LiveData<MovieUiModel> = _selectedMovie

    fun fetchMoviesTopRated() {
        viewModelScope.launch(Dispatchers.IO) {
            val moviesResult = repository.fetchTopRatedMovies()
            _topRatedMovies.postValue(moviesResult)
        }
    }

    fun addMovieClick(id: Int, errorCallback: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovie(id, errorCallback)
            val cachedMovies = repository.getAllMoviesDb()
            _topRatedMovies.postValue(ResponseResult.Success(cachedMovies))
        }
    }


    fun updateMovieDb(movie: MovieUiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMovieDb(movie)
            val cachedMovies = repository.getAllMoviesDb()
            _topRatedMovies.postValue(ResponseResult.Success(cachedMovies))
        }
    }

    fun getMovieByIdDb(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val movie = repository.getMovieByIdDb(id) ?: return@launch
            _selectedMovie.postValue(movie)
        }
    }

    fun deleteMovieDb(movie: MovieUiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMovieDb(movie)
            val cachedMovies = repository.getAllMoviesDb()
            _topRatedMovies.postValue(ResponseResult.Success(cachedMovies))
        }
    }

    fun clearMoviesDb() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMoviesDb()
            val cachedMovies = repository.getAllMoviesDb()
            _topRatedMovies.postValue(ResponseResult.Success(cachedMovies))
        }
    }
}
