package kz.app.roomretrofit.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.app.roomretrofit.BuildConfig
import kz.app.roomretrofit.R
import kz.app.roomretrofit.presentation.models.MovieUiModel

class MovieAdapter(
    private var movies: List<MovieUiModel>,
    private val onMovieClick: (Int) -> Unit,
    private val onDeleteClick: (MovieUiModel) -> Unit,
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterImageView: ImageView = itemView.findViewById(R.id.image_view_poster)
        val deleteImageView: ImageView = itemView.findViewById(R.id.image_view_delete)
        val titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        val ratingTextView: TextView = itemView.findViewById(R.id.text_view_rating)
        val releaseTextView: TextView = itemView.findViewById(R.id.text_view_release_date)
        val descriptionTextView: TextView = itemView.findViewById(R.id.text_view_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.titleTextView.text = movie.title
        holder.ratingTextView.text = movie.rating
        holder.releaseTextView.text = movie.releaseDate
        holder.descriptionTextView.text = movie.description

        val imageUrl = "${BuildConfig.posterPath}${movie.posterPath}"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.posterImageView)

        holder.deleteImageView.setOnClickListener {
            onDeleteClick(movie)
        }

        holder.itemView.setOnClickListener { onMovieClick(movie.id) }
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<MovieUiModel>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    fun clearMovies() {
        movies = emptyList()
        notifyDataSetChanged()
    }
}
