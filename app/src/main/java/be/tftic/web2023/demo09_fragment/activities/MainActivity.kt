package be.tftic.web2023.demo09_fragment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import be.tftic.web2023.demo09_fragment.R
import be.tftic.web2023.demo09_fragment.databinding.ActivityMainBinding
import be.tftic.web2023.demo09_fragment.fragments.MovieDetailFragment
import be.tftic.web2023.demo09_fragment.fragments.MovieListFragment
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadInitialFragment()

        // Snackbar.make(findViewById(R.id.layout_main), "Test", Snackbar.LENGTH_LONG).show()
        // Snackbar.make(binding.layoutMain, "Test", Snackbar.LENGTH_LONG).show()
    }

    private fun loadInitialFragment() {

        val fragList: MovieListFragment = MovieListFragment.newInstance()
        fragList.setOnMovieSelectListener { movieId -> openDetailFragment(movieId)}

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fcv_main_content, fragList)
        }
    }

    private fun openDetailFragment(movieId: Long) {

         val fragDetail : MovieDetailFragment = MovieDetailFragment.newInstance(movieId)

         supportFragmentManager.commit {
             setCustomAnimations(
                 android.R.anim.slide_in_left,
                 android.R.anim.fade_out,
                 android.R.anim.fade_in,
                 android.R.anim.slide_out_right
             )
             setReorderingAllowed(true)
             replace(R.id.fcv_main_content, fragDetail)
             addToBackStack(null)
         }
    }
}