package be.tftic.web2023.demo09_fragment.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import be.tftic.web2023.demo09_fragment.R
import be.tftic.web2023.demo09_fragment.databinding.FragmentMovieListBinding
import be.tftic.web2023.demo09_fragment.models.Movie
import be.tftic.web2023.demo09_fragment.services.MovieService
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass.
 * Use the [MovieListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieListFragment private constructor(): Fragment(),
    AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    companion object {
        @JvmStatic
        fun newInstance() : MovieListFragment {
            return MovieListFragment()
        }
    }

    // region Listener
    fun interface OnMovieSelectListener {
        fun onMovieSelected(movieId : Long)
    }

    private var onMovieSelectListener : OnMovieSelectListener? = null

    fun setOnMovieSelectListener(listener: OnMovieSelectListener) {
        onMovieSelectListener = listener
    }
    // endregion

    private lateinit var binding : FragmentMovieListBinding
    private lateinit var movieService: MovieService
    private lateinit var movies : MutableList<Movie>
    private lateinit var adapter : ArrayAdapter<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieService = MovieService()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMovieListBinding.inflate(inflater, container, false)

        // Populer la list
        movies = movieService.getAll().toMutableList()

        adapter = ArrayAdapter<Movie>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            movies
        )
        binding.lvFragMovieList.adapter = adapter

        binding.lvFragMovieList.setOnItemClickListener(this)
        binding.lvFragMovieList.setOnItemLongClickListener(this)

        // Return View
        return binding.root
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent == null) {
            return
        }

        val movie = parent.adapter.getItem(position) as Movie
        onMovieSelectListener?.onMovieSelected(movie.Id)
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        if(parent == null) {
            return false
        }

        val movie = parent.adapter.getItem(position) as Movie

        val dialogBuilder = AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.alert_delete_title)
            setMessage(getString(R.string.alert_delete_message).format(movie.Title))

            setPositiveButton(android.R.string.ok) { dialog, id -> deleteMovie(movie.Id) }
            setNegativeButton(android.R.string.cancel) { dialog, id -> /* On fait rien */ }

            setCancelable(false)
        }

        dialogBuilder.create().show()

        return true
    }

    private fun deleteMovie(movieId: Long) {

        // On supprime l'element dans la "copie" de la liste
        // - On cherche l'index et l'element
        val backupIndex = movies.indexOfFirst { movie -> movie.Id == movieId }
        val backupMovie = movies[backupIndex]
        // - On le vire de la liste
        movies.removeAt(backupIndex)
        // - On met à jour le visuel
        adapter.notifyDataSetChanged()

        // Snack bar pour annuler l'operation
        showUndoSnackBar(movieId, backupIndex, backupMovie)
    }

    fun showUndoSnackBar(movieId: Long, backupIndex: Int, backupMovie : Movie) : Unit {

        // Création de la SnackBar
        val snackUndo  = Snackbar.make(
            requireContext(),
            binding.layoutFragList,
            getString(R.string.snack_movie_removed),
            Snackbar.LENGTH_INDEFINITE
        )

        // Customisation
        snackUndo.setBackgroundTint(resources.getColor(R.color.pink, null))
        snackUndo.setTextColor(resources.getColor(R.color.black, null))
        snackUndo.setActionTextColor(resources.getColor(R.color.blue, null))

        // Ajout d'un bouton action
        snackUndo.setAction(R.string.btn_undo) { view ->
            // Traitement quand on click le "bouton" annuler de la Snackbar
            // - On restaure la liste
            movies.add(backupIndex, backupMovie)
            // - On met à jour le visuel
            adapter.notifyDataSetChanged()
        }

        // Ajout d'un comportement sur disparition du Snackbar (hors Action)
        snackUndo.addCallback(object : Snackbar.Callback() {

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                Log.d("Aziza", "Event : " + event)

                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    // On utilise le service pour réelement supprimer le movie
                    val isDeleted = movieService.delete(movieId)
                }
            }
        })

        snackUndo.show()
    }

}