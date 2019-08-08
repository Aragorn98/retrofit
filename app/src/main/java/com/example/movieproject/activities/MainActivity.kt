package com.example.movieproject.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import com.example.movieproject.LangPref
import com.example.movieproject.R
import com.example.movieproject.adapters.OnMoreClickedListener
import com.example.movieproject.adapters.ParentAdapter
import com.example.movieproject.api.models.*
import com.example.movieproject.api.models.DeleteSession
import com.example.movieproject.listeners.*
import com.example.movieproject.loaders.*
import com.example.movieproject.loaders.CreateSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MovieLoadListener, MovieClickListener,
    OnMoreClickedListener,
    GenreLoadListener, AdapterView.OnItemSelectedListener, ByGenreLoadListener, RequestTokenLoadListener,
    SessionListener, DeleteSessionListener{



    private val loader by lazy { MovieLoader(this) }
//    private val moviesAdapter by lazy { MoviesAdapter() }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    val list = mutableListOf<Result>()
    private lateinit var requestToken: String
    var sessionId: String? = null

    companion object {
        private const val FAVOURITES = "favourites"

        fun start(context: Context) {
            context.startActivity(
                Intent(context,
                    MainActivity::class.java)
            )
        }
    }
    private val firebaseCloudstore by lazy { FirebaseFirestore.getInstance() }
    private val favourites by lazy { firebaseCloudstore.collection(MainActivity.FAVOURITES) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val user = firebaseAuth.currentUser

        if (user == null) {
            LoginActivity.start(this)
            finish()
        }

//        MovieLoader(this).loadUpcomingMovies()
        createRequestToken.setOnClickListener{
            RequestTokenLoader(this).loadRequestToken()

        }

        createSession.setOnClickListener{
            CreateSession(this, RequestTokenClass(this.requestToken)).createSession()
        }

        deleteSession.setOnClickListener{
            if(sessionId != null){
                com.example.movieproject.loaders.DeleteSession(this, SessionIdClass(sessionId.toString())).deleteSession()
            }
            else Toast.makeText(this, "There is no session", Toast.LENGTH_LONG).show()
        }

        eng.setOnClickListener { LangPref.lang = "en"
        Log.d("agryn", "en")
        }
        eng.setOnClickListener { LangPref.lang = "ru"
            Log.d("agryn", "ru")
        }
        favourite_movies.setOnClickListener {
            var movies = ArrayList<Movie>()
            favourites.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        document.forEach { snapshot ->
                            Log.d("argyn", snapshot.toString())
                            movies.add(snapshot.toObject(Movie::class.java))
                        }


                        val result = Result("Favourites", 0, movies, 0, 0)

                        Log.d("argyn", result.toString())
                        Log.d("argyn", movies.toString())
                        MovieListActivity.start(this, result)

                        Log.d("argyn", " not null")
                    } else {
                        Log.d("argyn", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("argyn", "get failed with ", exception)
                }
        }



        genres_spinner.setSelection(0, false)
        GenreLoader(this).loadGenres()
        MovieLoader(this).loadTopRatedMovies()
        MovieLoader(this).loadPopularMovies()
        MovieLoader(this).loadUpcomingMovies()
        MovieLoader(this).loadNowPlayingMovies()
        //initUI()
//        loader.loadTopRatedMovies()
//        loader.loadPopularMovies()

    }

    private fun initUI() {

//        moviesAdapter.setListener(this)
//
//        with(movies_list) {
//            layoutManager = LinearLayoutManager(context)
//            adapter = moviesAdapter
//        }


//        with(movies_list){
//            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.VERTICAL, false)
//            adapter = ParentAdapter(list)
//        }

    }


    override fun onSessionDeleted(response: DeleteSession) {
        Toast.makeText(this, response.success.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onSessionDeleteError(throwable: Throwable) {
        Log.d("argyn", throwable.message)
    }

    override fun onSessionCreated(session: com.example.movieproject.api.models.CreateSession) {
        sessionId = session.session_id
        Log.d("argyn", session.session_id)
        Toast.makeText(this, "Session created", Toast.LENGTH_LONG).show()
    }


    override fun onSessionCreateError(throwable: Throwable) {
        Log.d("argyn", throwable.message)
    }

    override fun onRequestTokenLoaded(requestToken: RequestToken) {
        this.requestToken = requestToken.request_token
        val url = "https://www.themoviedb.org/authenticate/" + this.requestToken
        Log.d("argyn", this.requestToken)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

    }

    override fun onRequestTokenLoadError(throwable: Throwable) {
        Log.d("argyn", throwable.message)
    }

    override fun onByGenreLoaded(movies: Result) {
        MovieListActivity.start(this, movies)
    }

    override fun onByGenreLoadError(throwable: Throwable) {
        Log.d("argyn", throwable.message)
    }

    val genreName = mutableListOf<String>()
    lateinit var genres: List<Genre>


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        Toast.makeText(this, genreName[position], Toast.LENGTH_LONG).show()
        ByGenreLoader(this).loadMoviesByGenre(genres[position].id.toString(), genres[position].name)
    }


    override fun onGenresLoaded(genres: List<Genre>) {
        this.genres = genres
        for (genre: Genre in genres){
            genreName.add(genre.name)
        }
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genreName)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genres_spinner!!.adapter = arrayAdapter
        genres_spinner.onItemSelectedListener = this
    }

    override fun onGenresLoadError(throwable: Throwable) {

    }

    override fun onMoreClicked(result: Result) {
        MovieListActivity.start(this, result)
    }

    override fun onMoviesLoaded(movies: Result) {
        list.add(movies)
        Log.d("argyn", movies.type)
        if(list.size == 4) {
            with(rv_parent){
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.VERTICAL, false)
                adapter = ParentAdapter(list, this@MainActivity, this@MainActivity)
            }
        }
//        moviesAdapter.setMovies(movies)
    }

    override fun onMoviesLoadError(throwable: Throwable) {
        Toast.makeText(this, throwable.message,
            Toast.LENGTH_LONG).show()
    }

    override fun onMovieClicked(movie: Movie) {
        Log.d("argyn", movie.id.toString())
        MovieDescription.start(this, movie.id.toString(), movie)
    }
}
