package kz.app.roomretrofit

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.app.roomretrofit.data.database.AppDatabase
import kz.app.roomretrofit.data.database.MovieRepository
import kz.app.roomretrofit.data.network.ResponseResult
import kz.app.roomretrofit.data.network.RetrofitService
import kz.app.roomretrofit.presentation.MovieAdapter
import kz.app.roomretrofit.presentation.MovieViewModel
import kz.app.roomretrofit.presentation.MovieViewModelFactory
import kz.app.roomretrofit.presentation.models.MovieUiModel

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val movieDao = AppDatabase.getDatabase(application).movieDao()
        val retrofitService = RetrofitService.createService()
        val repository = MovieRepository(movieDao, retrofitService)

        val factory = MovieViewModelFactory(repository)

        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_movies)
        movieAdapter = MovieAdapter(
            emptyList(),
            onMovieClick = movieViewModel::getMovieByIdDb,
            onDeleteClick = ::onDeleteClick
        )
        recyclerView.adapter = movieAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val databaseInfoTextView = findViewById<TextView>(R.id.text_view_database_info)

        movieViewModel.topRatedMovies.observe(this) { result ->
            when (result) {
                is ResponseResult.Success -> {
                    movieAdapter.updateMovies(result.data)
                    databaseInfoTextView.text = "Database size:\n${result.data.size}"
                }

                is ResponseResult.Error -> {
                    Log.d("Error", "Error: ${result.exception}")
                    Toast.makeText(this, "Error: ${result.exception}", Toast.LENGTH_LONG).show()
                }
            }
        }

        movieViewModel.selectedMovie.observe(this) { movie ->
            showEditDialog(movie)
        }

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        findViewById<FloatingActionButton>(R.id.button_add_movie).setOnClickListener {
            showAddDialog()
        }

        findViewById<FloatingActionButton>(R.id.button_remove_movies).setOnClickListener {
            movieViewModel.clearMoviesDb()
        }

        loadData()
    }

    private fun loadData() {
        movieViewModel.fetchMoviesTopRated()
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_movie, null)
        val movieIdEditText = dialogView.findViewById<EditText>(R.id.edit_text_title)

        AlertDialog.Builder(this)
            .setTitle("Add Movie")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val enteredText = movieIdEditText.text.toString()
                if (enteredText.isNotBlank()) {
                    movieViewModel.addMovieClick(enteredText.toInt()) { errorText ->
                        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditDialog(movie: MovieUiModel) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_movie, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.edit_text_title)
        val ratingEditText = dialogView.findViewById<EditText>(R.id.edit_text_rating)

        titleEditText.setText(movie.title)
        ratingEditText.setText(movie.rating)

        AlertDialog.Builder(this)
            .setTitle("Edit Movie")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedMovie = movie.copy(
                    title = titleEditText.text.toString(),
                    rating = ratingEditText.text.toString()
                )
                movieViewModel.updateMovieDb(updatedMovie)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun refreshData() {
        swipeRefreshLayout.isRefreshing = true
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                movieAdapter.clearMovies()
                delay(500) //fake loading to show that is working properly
                loadData()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun onDeleteClick(movie: MovieUiModel) {
        movieViewModel.deleteMovieDb(movie)
    }
}