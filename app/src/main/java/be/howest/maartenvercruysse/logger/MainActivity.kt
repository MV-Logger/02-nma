package be.howest.maartenvercruysse.logger

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import be.howest.maartenvercruysse.logger.databinding.ActivityMainBinding
import be.howest.maartenvercruysse.logger.ui.MainViewModel
import be.howest.maartenvercruysse.logger.ui.MainViewModelFactory
import be.howest.maartenvercruysse.logger.ui.books.BookFragment
import be.howest.maartenvercruysse.logger.ui.books.BookFragmentDirections
import be.howest.maartenvercruysse.logger.ui.dialog.BookDialogFragment
import be.howest.maartenvercruysse.logger.ui.home.HomeFragment
import be.howest.maartenvercruysse.logger.ui.home.HomeFragmentDirections
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            createDialog()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel = ViewModelProvider(this, MainViewModelFactory(this.application))
            .get(MainViewModel::class.java)

        val menu = navView.menu.addSubMenu("Books")

        viewModel.repo.books.observe(this, { list ->
            list?.let { m ->
                menu.clear()
                m.forEach {
                    menu.add(0, it.id, 0, it.name)
                }
            }
        })

        // This logic, controls which fragment will be opened when an item is clicked
        navView.setNavigationItemSelectedListener {
            val id = it.itemId
            val fragment = getForegroundFragment()
            Log.d("nav", id.toString())

            val action: NavDirections = if (id == R.id.nav_home) { // home button
                if (fragment !is HomeFragment) { // if not currently home
                    BookFragmentDirections.actionBookFragmentToHomeFragment() // go to home
                }else{
                    return@setNavigationItemSelectedListener false
                }
            } else {
                when (fragment) { // BookFragments
                    is HomeFragment -> HomeFragmentDirections.actionHomeFragmentToBookFragment(id)
                    is BookFragment -> BookFragmentDirections.actionBookFragmentSelf(id)
                    else -> throw NullPointerException("Unknown fragment (shouldn't be possible)")
                }
            }
            navController.navigate(action)
            drawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.repo.logout()
                true
            }
            else -> false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun createDialog() {
        BookDialogFragment(viewModel.repo).show(supportFragmentManager, "BookDialogFragment")
    }

    fun getForegroundFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

}