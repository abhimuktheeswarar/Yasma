package msa.yasma

import android.os.Bundle
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

    }

    override fun onSupportNavigateUp() = navController.navigateUp()


}
