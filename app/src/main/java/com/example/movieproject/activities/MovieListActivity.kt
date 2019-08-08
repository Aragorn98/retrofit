package com.example.movieproject.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.movieproject.R
import com.example.movieproject.adapters.MoviesAdapter
import com.example.movieproject.api.models.Movie
import com.example.movieproject.api.models.Result
import com.example.movieproject.listeners.MovieClickListener
import com.example.movieproject.listeners.MovieLoadListener
import com.example.movieproject.loaders.MovieLoader
import kotlinx.android.synthetic.main.activity_movie_list.*

class MovieListActivity : AppCompatActivity(), MovieLoadListener,
    MovieClickListener {

    private val loader by lazy { MovieLoader(this) }
    private val moviesAdapter by lazy { MoviesAdapter() }
    lateinit var movies: Result

    companion object {
        private const val ARG_ID = "movies"

        fun start(context: Context, list: Result) {
            context.startActivity(
                Intent(context,
                    MovieListActivity::class.java).apply {
                    putExtra(ARG_ID, list)
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        initUI()
//        loader.loadPopularMovies()
    }

    private fun initUI() {
        moviesAdapter.setListener(this)

        with(movies_list) {
            layoutManager = android.support.v7.widget.LinearLayoutManager(context)
            adapter = moviesAdapter
        }
        movies = intent.getParcelableExtra(ARG_ID)
        loadMovieList(movies)
    }

    private fun loadMovieList(movies: Result) {
        moviesAdapter.setMovies(movies.results)
    }

    override fun onMoviesLoaded(movies: Result) {
//        moviesAdapter.setMovies(movies.results)
    }

    override fun onMoviesLoadError(throwable: Throwable) {
        Toast.makeText(this, throwable.message,
            Toast.LENGTH_LONG).show()
    }

    override fun onMovieClicked(movie: Movie) {
//        var sharedPreferences: SharedPreferences
        MovieDescription.start(this, movie.id.toString(), movie)
//        Toast.makeText(this, movie.toString(), Toast.LENGTH_LONG).show()
    }
}
