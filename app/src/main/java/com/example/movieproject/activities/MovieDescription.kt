package com.example.movieproject.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.movieproject.R
import com.example.movieproject.adapters.CreditsAdapter
import com.example.movieproject.adapters.LogosAdapter
import com.example.movieproject.adapters.OnMoreClickedListener
import com.example.movieproject.adapters.ParentAdapter
import com.example.movieproject.api.models.*
import com.example.movieproject.listeners.*
import com.example.movieproject.loaders.CreditLoader
import com.example.movieproject.loaders.MovieDescLoader
import com.example.movieproject.loaders.MovieLoader
import com.example.movieproject.loaders.VideoLoader
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_description.*

class MovieDescription : AppCompatActivity(), MovieDescLoadListener,
    MovieLoadListener,
    MovieClickListener,
    OnMoreClickedListener, CreditLoadListener, VideoLoadListener {


    override fun onCreditsLoaded(credits: Credits) {
        var list = credits.cast
        list = list.subList(0,3)
        creditsAdapter.setCasts(list)
    }

    override fun onCreditsLoadError(throwable: Throwable) {

    }

    override fun onMovieClicked(movie: Movie) {
        start(this, movie.id.toString(), movie)
    }

    override fun onMoreClicked(result: Result) {
        MovieListActivity.start(this, movies)
    }

    lateinit var movieDetails: MovieDetails
    lateinit var movies: Result
    private val viewPool = RecyclerView.RecycledViewPool()
    val list = mutableListOf<Result>()
    private val creditsAdapter by lazy { CreditsAdapter() }
    private val logosAdapter by lazy { LogosAdapter() }
    private val firebaseCloudstore by lazy { FirebaseFirestore.getInstance() }
    private val favourites by lazy { firebaseCloudstore.collection(FAVOURITES) }


    override fun onMoviesLoaded(movies: Result) {
        this.movies = movies
        loadRecommendedMovies(movies)
    }

    private fun loadRecommendedMovies(movies: Result) {
//        val childLayoutManager = LinearLayoutManager(
//            recommended_movies.context, LinearLayout.HORIZONTAL, false)
//        childLayoutManager.initialPrefetchItemCount = 4
//        recommended_movies.apply {
//            layoutManager = childLayoutManager
//            adapter = ChildAdapter(movies.results)
//            setRecycledViewPool(viewPool)
//        }
        list.add(movies)
        with(recommended_movies){
            layoutManager = LinearLayoutManager(this@MovieDescription, LinearLayout.VERTICAL, false)
            adapter =
                ParentAdapter(list, this@MovieDescription, this@MovieDescription)
        }

    }

    override fun onMoviesLoadError(throwable: Throwable) {

    }

    override fun onMovieDescLoaded(movies: MovieDetails) {
        movieDetails = movies
        loadDetails(movieDetails)
        logosAdapter.setLogos(movieDetails.production_companies)
    }

    private fun loadDetails(movieDetails: MovieDetails) {
        Picasso.get().load("http://image.tmdb.org/t/p/w780" + movieDetails.poster_path).into(movie_poster)
        movie_title.text = movieDetails.title
        movie_overview.text = movieDetails.overview

    }

    override fun onMovieDescLoadError(throwable: Throwable) {
        Log.d("taaag", throwable.message)
    }

    companion object {
        private const val FAVOURITES = "favourites"

        private const val ARG_ID = "movie_id"
        private const val ARG_MOVIE = "movie"

        fun start(context: Context, id: String, movie: Movie) {
            context.startActivity(
                Intent(context,
                    MovieDescription::class.java).apply {
                    putExtra(ARG_ID, id)
                    putExtra(ARG_MOVIE, movie)
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_description)

        initUI()
    }

    private fun initUI() {


        with(credits_list) {
            layoutManager = LinearLayoutManager(context)
            adapter = creditsAdapter
        }
        with(logos_list) {
            layoutManager = LinearLayoutManager(context)
            adapter = logosAdapter
        }
        var movieId: String = intent.getStringExtra(ARG_ID)
        var movie: Movie = intent.getParcelableExtra(ARG_MOVIE)
        CreditLoader(this
        ).loadTopCredits(movieId)

        addToFavourites.setOnClickListener{
            favourites.document(movie.id.toString()).set(movie).addOnCompleteListener {
                    task ->
                run {
                    if (task.isSuccessful) {
                        Toast.makeText(this, R.string.success_message,
                            Toast.LENGTH_LONG).show()


                    } else {
                        Toast.makeText(this, task.exception?.message,
                            Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
        removeFromFavourites.setOnClickListener{
            favourites.document(movie.id.toString()).delete().addOnSuccessListener {
                Log.d("argyn", "deleted")
            }.addOnFailureListener{
                Log.d("argyn", "not deleted")
            }
        }


        MovieDescLoader(this).loadMovieDesc(movieId)
        MovieLoader(this).loadRecommendedMovies(movieId)
        MovieLoader(this).loadSimilarMovies(movieId)
        VideoLoader(this).loadVideos(movieId)
    }


    override fun onVideosLoaded(video: Video) {
        movie_video.setOnClickListener(View.OnClickListener {
            var intent = YouTubeStandalonePlayer.createVideoIntent(this, "AIzaSyB2K-Jq5IF_4GxOwROSrTBfEuISnLKe4nM",
                video.results[0].key)
            startActivity(intent)
        })


    }

    override fun onVideosLoadError(throwable: Throwable) {

    }
}
