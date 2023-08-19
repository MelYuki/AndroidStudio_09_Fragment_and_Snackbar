package be.tftic.web2023.demo09_fragment.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import be.tftic.web2023.demo09_fragment.R
import be.tftic.web2023.demo09_fragment.databinding.FragmentMovieDetailBinding
import be.tftic.web2023.demo09_fragment.models.Movie
import be.tftic.web2023.demo09_fragment.services.MovieService
import com.google.android.material.snackbar.Snackbar

private const val ARG_MOVIE_ID = "arg_movie_id"

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetailFragment private constructor() : Fragment() {

    private var movieId: Long = -1
    private lateinit var binding : FragmentMovieDetailBinding
    companion object {
        @JvmStatic
        fun newInstance(movieId : Long) =
            MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_MOVIE_ID, movieId)
                }
            }
    }


    private lateinit var movieService: MovieService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().let { bundle ->
            movieId = bundle.getLong(ARG_MOVIE_ID)
        }

        movieService = MovieService()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        // Populate view
        populateView()

        // Return View
        return binding.root
    }

    private fun populateView() {
        val movie : Movie? = movieService.getDetail(movieId)

        if(movie == null) {
            binding.lvFragMovieDetailTitle.setText(R.string.film_not_found)
            binding.lvFragMovieDetailRelease.setText("")
            binding.lvFragMovieDetailDesc.setText("")
            return
        }

        binding.lvFragMovieDetailTitle.setText(movie.Title)
        binding.lvFragMovieDetailRelease.setText(getString(R.string.release_date).format(movie.ReleaseYear))
        binding.lvFragMovieDetailDesc.setText(movie.Description ?: getString(R.string.movie_no_desc)) // Coalesce

        Toast.makeText(requireContext(), "MovieId : %s".format(movieId), Toast.LENGTH_LONG).show()
    }


}