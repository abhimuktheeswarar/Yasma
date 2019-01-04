package msa.yasma

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Abhi Muktheeswarar.
 */

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnNavigatedListener { controller, destination ->

            when (destination.id) {

                R.id.postListFragment, R.id.albumListFragment -> {

                    bottomNavigationView.visibility = View.VISIBLE
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                }

                R.id.postDetailFragment, R.id.albumDetailFragment, R.id.imageViewerFragment -> {

                    bottomNavigationView.visibility = View.GONE
                    supportActionBar?.setDisplayShowTitleEnabled(false)
                }
            }
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp()


}
